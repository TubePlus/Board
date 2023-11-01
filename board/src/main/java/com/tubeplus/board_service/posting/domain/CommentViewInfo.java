package com.tubeplus.board_service.posting.domain;


import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class CommentViewInfo {

    private final long commentId;

    private final Long parentId;

    private final String contents;

    private final Long childCount;

    private final String commenterUuid;

}
