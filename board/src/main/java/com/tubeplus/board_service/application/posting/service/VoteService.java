package com.tubeplus.board_service.application.posting.service;

import com.tubeplus.board_service.adapter.web.error.BusinessException;
import com.tubeplus.board_service.adapter.web.error.ErrorCode;
import com.tubeplus.board_service.application.posting.domain.vote.Vote;
import com.tubeplus.board_service.application.posting.port.in.PostingVoteUseCase;
import com.tubeplus.board_service.application.posting.port.in.WebVoteUseCase;
import com.tubeplus.board_service.application.posting.port.out.PostingPersistable;
import com.tubeplus.board_service.application.posting.port.out.VoteEventPublishable;
import com.tubeplus.board_service.application.posting.port.out.VotePersistable;
import com.tubeplus.board_service.application.posting.port.out.VotePersistable.FindVoteDto;
import com.tubeplus.board_service.application.posting.port.out.VotePersistable.SaveVoteDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@SuppressWarnings("UnnecessaryLocalVariable")
@Service
@RequiredArgsConstructor
public class VoteService
        implements WebVoteUseCase, PostingVoteUseCase {


    private final VotePersistable votePersistence;
    private final PostingPersistable postingPersistence;

    private final VoteEventPublishable eventPublisher;


    /**///command query both
    //posting use case
    public Optional<Vote> findUserVote(Long postingId, String userUuid) {

        FindVoteDto dto = FindVoteDto.of(postingId, userUuid);

        Optional<Vote> optionalUserVote
                = votePersistence.findVote(dto)
                .ifExceptioned.thenThrow(ErrorCode.FIND_ENTITY_FAILED);

        return optionalUserVote;
    }


    /**///command
    //web use case
    @Override
    public Long votePosting(Vote voteInfo) {

        /**/
        filterAlreadyVoted(voteInfo);

        /**/
        SaveVoteDto dto = SaveVoteDto.builtFrom(voteInfo);

        Vote savedVote
                = votePersistence.saveVote(dto)
                .ifExceptioned.thenThrow(ErrorCode.SAVE_ENTITY_FAILED);

        if (savedVote == null)
            throw new BusinessException(ErrorCode.SAVE_ENTITY_FAILED);

        /**/
        eventPublisher.publishPostingVoted(postingPersistence, savedVote);

        /**/
        return savedVote.getId();
    }

    protected void filterAlreadyVoted(Vote voteInfo) {

        long postingId = voteInfo.getPostingId();
        String voterUuid = voteInfo.getVoterUuid();

        findUserVote(postingId, voterUuid)
                .ifPresent(vote -> {
                    throw new BusinessException(ErrorCode.SAVE_ENTITY_FAILED, "User already voted");
                });
    }


    @Override
    public long updateVote(Vote updateInfo) {

        /**/
        Boolean updated
                = votePersistence.updateVote(updateInfo)
                .ifExceptioned.thenThrow(ErrorCode.UPDATE_ENTITY_FAILED);

        if (!updated)
            throw new BusinessException(ErrorCode.UPDATE_ENTITY_FAILED);


        /**/
        eventPublisher.publishVoteUpdated(postingPersistence, updateInfo);


        /**/
        Long updatedTotalVote
                = votePersistence.getTotalVote(updateInfo.getPostingId())
                .ifExceptioned.thenThrow(ErrorCode.FIND_ENTITY_FAILED);


        return updatedTotalVote;
    }


    @Override
    public long deleteVote(Long voteId) {

        /**/
        Vote deletedVote
                = votePersistence.findVote(voteId)
                .ifExceptioned.thenThrow(ErrorCode.FIND_ENTITY_FAILED)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.FIND_ENTITY_FAILED, "Can't find vote"
                ));

        /**/
        votePersistence.deleteVote(voteId)
                .ifExceptioned.thenThrow(ErrorCode.DELETE_ENTITY_FAILED);

        /**/
        eventPublisher.publishVoteDeleted(postingPersistence, deletedVote);

        /**/
        Long updatedTotalVote;

        long votePostingId
                = deletedVote.getPostingId();

        updatedTotalVote
                = votePersistence.getTotalVote(votePostingId)
                .ifExceptioned.thenThrow(ErrorCode.FIND_ENTITY_FAILED);

        return updatedTotalVote;
    }


}
