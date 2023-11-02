package com.tubeplus.board_service.posting.domain.vote;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Vote {
    private long id;
    private final long postingId;
    private final String voterUuid;
    private final VoteType voteType;
}
