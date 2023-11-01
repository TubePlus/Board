package com.tubeplus.board_service.posting.port.in;

import com.tubeplus.board_service.posting.domain.vote.PostingVote;

public interface VoteUsecase {
    //vote
    long votePosting(PostingVote vote);

    long modifyPostingVote(PostingVote vote);

    long cancelVote();
}
