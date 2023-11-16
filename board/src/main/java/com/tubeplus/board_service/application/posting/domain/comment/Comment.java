package com.tubeplus.board_service.application.posting.domain.comment;


import lombok.Builder;
import lombok.Data;


@Data(staticConstructor = "of")
public class Comment {

    private final Long id;
    private final Long postingId;
    private final CommentViewInfo viewInfo;

    @Data
    @Builder
    public static class CommentViewInfo {
        private final Long parentId;
        private final boolean hasChild;
        private final String content;
        private final String commenterUuid;
    }
}
