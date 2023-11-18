package com.tubeplus.board_service.application.posting.port.in;

import com.tubeplus.board_service.application.posting.domain.vote.Vote;

public interface WebVoteUseCase {
    //vote
    Long votePosting(Vote vote);

    long updateVote(Vote updateInfo);

    long deleteVote(Long voteId);
}
