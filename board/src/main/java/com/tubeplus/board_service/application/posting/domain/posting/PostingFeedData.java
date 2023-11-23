package com.tubeplus.board_service.application.posting.domain.posting;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class PostingFeedData {

    private final Long id;
    private final String authorUuid;
    private final long voteCount;
    private final String title;
    private final boolean withImage;
    private final long commentsCount;


    public static PostingFeedData builtFrom(Posting posting, long commentsCount) {

        return PostingFeedData.builder()
                .id(posting.getId())
                .authorUuid(posting.getAuthorUuid())
                .voteCount(posting.getVoteCount())
                .title(posting.getTitle())
                .withImage(posting.isWithImage())
                .commentsCount(commentsCount)
                .build();
    }

}
