package com.tubeplus.board_service.application.posting.domain.comment;


import lombok.Builder;
import lombok.Data;


@Data(staticConstructor = "of")
public class Comment {

    private final Long id;
    private final CommentViewInfo viewInfo;

    @Data
    @Builder
    public static class CommentViewInfo {

        private final Long postingId;
        private final Long parentId;
        private final boolean hasChild;
        private final String contents;
        private final String commenterUuid;

    }
}
