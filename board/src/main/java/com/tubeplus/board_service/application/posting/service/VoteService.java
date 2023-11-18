package com.tubeplus.board_service.application.posting.service;

import com.tubeplus.board_service.adapter.web.error.BusinessException;
import com.tubeplus.board_service.adapter.web.error.ErrorCode;
import com.tubeplus.board_service.application.posting.domain.posting.Posting;
import com.tubeplus.board_service.application.posting.domain.vote.Vote;
import com.tubeplus.board_service.application.posting.port.in.PostingVoteUseCase;
import com.tubeplus.board_service.application.posting.port.in.WebVoteUseCase;
import com.tubeplus.board_service.application.posting.port.out.VotePersistable;
import com.tubeplus.board_service.application.posting.port.out.VotePersistable.FindVoteDto;
import com.tubeplus.board_service.application.posting.port.out.VotePersistable.SaveVoteDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class VoteService
        implements WebVoteUseCase, PostingVoteUseCase {


    private final VotePersistable votePersistence;

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

        //todo 카프카로 posting에 vote가 추가되었다는 이벤트를 보내야함

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

        //todo 카프카로 posting에 vote가 업데이트 되었다는 이벤트를 보내야함

        /**/
        Long updatedTotalVote
                = votePersistence.getTotalVote(updateInfo.getPostingId())
                .ifExceptioned.thenThrow(ErrorCode.FIND_ENTITY_FAILED);

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


        /**/
        votePersistence.deleteVote(voteId)
                .ifExceptioned.thenThrow(ErrorCode.DELETE_ENTITY_FAILED);

        //todo 카프카로 posting에 vote가 삭제되었다는 이벤트를 보내야함


        /**/
        Long deletedTotalVote
                = votePersistence.getTotalVote(votedPostingId)
                .ifExceptioned.thenThrow(ErrorCode.FIND_ENTITY_FAILED);

        return deletedTotalVote;
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
