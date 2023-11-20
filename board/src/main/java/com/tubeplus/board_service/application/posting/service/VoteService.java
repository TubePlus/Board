package com.tubeplus.board_service.application.posting.service;

import com.tubeplus.board_service.adapter.web.error.BusinessException;
import com.tubeplus.board_service.adapter.web.error.ErrorCode;
import com.tubeplus.board_service.application.posting.domain.posting.Posting;
import com.tubeplus.board_service.application.posting.domain.vote.Vote;
import com.tubeplus.board_service.application.posting.port.in.PostingVoteUseCase;
import com.tubeplus.board_service.application.posting.port.in.WebVoteUseCase;
import com.tubeplus.board_service.application.posting.port.out.PostingPersistable;
import com.tubeplus.board_service.application.posting.port.out.VotePersistable;
import com.tubeplus.board_service.application.posting.port.out.VotePersistable.FindVoteDto;
import com.tubeplus.board_service.application.posting.port.out.VotePersistable.SaveVoteDto;
import com.tubeplus.board_service.global.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class VoteService
        implements WebVoteUseCase, PostingVoteUseCase {


    private final VotePersistable votePersistence;
    private final PostingPersistable postingPersistence;
    private final KafkaProducer kafkaProducer;

    /**/
    //web use case
    @Override
    public Long votePosting(Vote vote) {

        SaveVoteDto dto = SaveVoteDto.builtFrom(vote);

        Vote savedVote
                = votePersistence.saveVote(dto)
                .ifExceptioned.thenThrow(ErrorCode.SAVE_ENTITY_FAILED);

        if (savedVote == null)
            throw new BusinessException(ErrorCode.SAVE_ENTITY_FAILED);

        try {
            Long communityId = postingPersistence.getPostingCommuId(vote.getPostingId())
                    .ifExceptioned.thenThrow(ErrorCode.FIND_ENTITY_FAILED);
            kafkaProducer.producerLikePosting(communityId, Long.valueOf(savedVote.getVoteType().getCode()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return savedVote.getId();
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
        Long updatedTotalVote
                = votePersistence.getTotalVote(updateInfo.getPostingId())
                .ifExceptioned.thenThrow(ErrorCode.FIND_ENTITY_FAILED);
        try {
            Long communityId = postingPersistence.getPostingCommuId(updateInfo.getPostingId())
                    .ifExceptioned.thenThrow(ErrorCode.FIND_ENTITY_FAILED);
            kafkaProducer.producerLikePosting(communityId, 2*Long.valueOf(updateInfo.getVoteType().getCode()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return updatedTotalVote;
    }

    @Override
    public long deleteVote(Long voteId) {

        /**/
        long votedPostingId;

        Posting votedPosting
                = votePersistence.getVotedPosting(voteId)
                .ifExceptioned.thenThrow(new BusinessException(
                                ErrorCode.FIND_ENTITY_FAILED, "Can't find voted Posting"
                        )
                );

        votedPostingId = votedPosting.getId();

        Vote vote = votePersistence.findVote(voteId)
                        .ifExceptioned.thenThrow(ErrorCode.FIND_ENTITY_FAILED)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.FIND_ENTITY_FAILED, "Can't find voted Posting"
                ));

        /**/
        votePersistence.deleteVote(voteId)
                .ifExceptioned.thenThrow(ErrorCode.DELETE_ENTITY_FAILED);
        try {
            Long communityId = postingPersistence.getPostingCommuId(votedPostingId)
                    .ifExceptioned.thenThrow(ErrorCode.FIND_ENTITY_FAILED);
            kafkaProducer.producerLikePosting(communityId, (-1)*Long.valueOf(vote.getVoteType().getCode()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        /**/
        Long updatedTotalVote
                = votePersistence.getTotalVote(votedPostingId)
                .ifExceptioned.thenThrow(ErrorCode.FIND_ENTITY_FAILED);

        return updatedTotalVote;
    }


    /**/
    //posting use case
    public Optional<Vote> findUserVote(Long postingId, String userUuid) {

        FindVoteDto dto = FindVoteDto.of(postingId, userUuid);

        Optional<Vote> optionalUserVote
                = votePersistence.findVote(dto)
                .ifExceptioned.thenThrow(ErrorCode.FIND_ENTITY_FAILED);

        return optionalUserVote;
    }

}
