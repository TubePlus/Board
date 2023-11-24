package com.tubeplus.board_service.application.posting.domain.posting;

import com.tubeplus.board_service.application.posting.port.in.PostingCommentUseCase;
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


    public static PostingPageView madeFrom(Posting posting, PostingCommentUseCase commentService) {
        return PostingPageView.builder()
                .id(posting.getId())
                .authorUuid(posting.getAuthorUuid())
                .voteCount(posting.getVoteCount())
                .pinned(posting.isPin())
                .title(posting.getTitle())
                .withImage(posting.isWithImage())
                .commentsCount(commentService.countComments(posting.getId()))
                .build();
    }

}
