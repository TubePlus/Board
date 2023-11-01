package com.tubeplus.board_service.posting.domain.posting;


import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class PostingViewInfo {

    private final String authorUuid;

    private final long voteCounts;

    private final String contents;

    private final String title;

    private final Long userVoteId;

    private final boolean haveImage;
}
