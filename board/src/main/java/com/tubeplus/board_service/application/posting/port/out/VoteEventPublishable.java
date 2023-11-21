package com.tubeplus.board_service.application.posting.port.out;

import com.tubeplus.board_service.application.posting.domain.vote.Vote;

public interface VoteEventPublishable {

    void publishPostingVoted(PostingPersistable postingPersistence, Vote savedVote);

    void publishVoteUpdated(PostingPersistable postingPersistence, Vote updateInfo);

    void publishVoteDeleted(PostingPersistable postingPersistence, Vote deletedVote);

}
