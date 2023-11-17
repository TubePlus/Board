package com.tubeplus.board_service.application.posting.service;

import com.tubeplus.board_service.adapter.web.error.ErrorCode;
import com.tubeplus.board_service.application.posting.domain.vote.Vote;
import com.tubeplus.board_service.application.posting.port.in.PostingVoteUseCase;
import com.tubeplus.board_service.application.posting.port.in.WebVoteUseCase;
import com.tubeplus.board_service.application.posting.port.out.VotePersistable;
import com.tubeplus.board_service.application.posting.port.out.VotePersistable.FindVoteDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class VoteService implements WebVoteUseCase, PostingVoteUseCase {

    private final VotePersistable votePersistence;


    //web use case
    @Override
    public long votePosting(Vote vote) {
        return 0;
    }


    @Override
    public long modifyPostingVote(Vote vote) {
        return 0;
    }

    @Override
    public long cancelVote() {
        return 0;
    }


    //posting use case
    public Optional<Vote> findUserVote(Long postingId, String userUuid) {

        FindVoteDto dto = FindVoteDto.of(postingId, userUuid);

        Optional<Vote> optionalUserVote
                = votePersistence.findVote(dto)
                .ifExceptioned.thenThrow(ErrorCode.FIND_ENTITY_FAILED);

        return optionalUserVote;
    }
}
