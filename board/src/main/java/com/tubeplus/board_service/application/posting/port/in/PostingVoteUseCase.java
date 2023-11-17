package com.tubeplus.board_service.application.posting.port.in;

import com.tubeplus.board_service.application.posting.domain.vote.Vote;

import java.util.Optional;

public interface PostingVoteUseCase {

    Optional<Vote> findUserVote(Long postingId, String userUuid);
}
