package com.tubeplus.board_service.application.posting.domain.posting;

import com.tubeplus.board_service.application.posting.port.in.PostingCommentUseCase;
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


    public static PostingFeedData madeFrom(Posting posting, PostingCommentUseCase commentService) {

        return PostingFeedData.builder()
                .id(posting.getId())
                .authorUuid(posting.getAuthorUuid())
                .voteCount(posting.getVoteCount())
                .title(posting.getTitle())
                .withImage(posting.isWithImage())
                .commentsCount(commentService.countComments(posting.getId()))
                .build();
    }

}
