package com.tubeplus.board_service.posting.port.in;

import com.tubeplus.board_service.posting.domain.vote.Vote;

public interface VoteUsecase {
    //vote
    long votePosting(Vote vote);

    long modifyPostingVote(Vote vote);

    long cancelVote();
}
