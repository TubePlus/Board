package com.tubeplus.board_service.application.posting.domain.posting;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostingPageView {

    private final Long id;
    private final String authorUuid;
    private final long voteCount;
    private final boolean pinned;
    private final String title;
    private final boolean withImage;
    private final long commentsCount;


    public static PostingPageView builtFrom(Posting posting, long commentsCount) {
        return PostingPageView.builder()
                .id(posting.getId())
                .authorUuid(posting.getAuthorUuid())
                .voteCount(posting.getVoteCount())
                .pinned(posting.isPin())
                .title(posting.getTitle())
                .withImage(posting.isWithImage())
                .commentsCount(commentsCount)
                .build();
    }

}
