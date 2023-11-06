package com.tubeplus.board_service.application.posting.port.in;

import com.tubeplus.board_service.application.posting.domain.vote.Vote;

public interface VoteUseCase {
    //vote
    long votePosting(Vote vote);

    long modifyPostingVote(Vote vote);

    long cancelVote();
}
