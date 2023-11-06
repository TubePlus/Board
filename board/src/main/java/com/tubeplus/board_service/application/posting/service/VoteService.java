package com.tubeplus.board_service.application.posting.service;

import com.tubeplus.board_service.adapter.web.error.ErrorCode;
import com.tubeplus.board_service.application.posting.domain.vote.Vote;
import com.tubeplus.board_service.application.posting.port.in.VoteUseCase;
import com.tubeplus.board_service.application.posting.port.out.VotePersistent;
import com.tubeplus.board_service.application.posting.port.out.VotePersistent.FindVoteDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class VoteService implements VoteUseCase {

    private final VotePersistent votePersistence;


    @Override
    public long votePosting(Vote vote) {
        return 0;
    }

    public Optional<Vote> findVote(FindVoteDto dto) {
        Optional<Vote> optionalUserVote
                = votePersistence.findVote(dto)
                .ifExceptioned.thenThrow(ErrorCode.FIND_ENTITY_FAILED);
        return optionalUserVote;
    }

    @Override
    public long modifyPostingVote(Vote vote) {
        return 0;
    }

    @Override
    public long cancelVote() {
        return 0;
    }
}
