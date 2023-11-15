package com.tubeplus.board_service.application.posting.domain.comment;


import lombok.Builder;
import lombok.Data;


@Data(staticConstructor = "of")
public class Comment {

    private final Long id;

    @Data
    @Builder
    public static class CommentViewInfo {

        private final Long postingId;
        private final Long parentId;
        private final String contents;
        private final Long childCount;
        private final String commenterUuid;

    }
}
