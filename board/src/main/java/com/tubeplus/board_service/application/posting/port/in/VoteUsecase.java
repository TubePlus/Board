package com.tubeplus.board_service.application.posting.port.in;

import com.tubeplus.board_service.application.posting.domain.vote.Vote;

public interface VoteUsecase {
    //vote
    long votePosting(Vote vote);

    long modifyPostingVote(Vote vote);

    long cancelVote();
}
