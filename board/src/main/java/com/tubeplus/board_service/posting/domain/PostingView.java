package com.tubeplus.board_service.posting.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class PostingView {

    private String authorUuid;
    @Builder.Default
    private long voteCounts = 0;
    private String contents;
    private String title;
    private boolean voted;
    private boolean haveImage;

}
