package com.tubeplus.board_service.posting.port.in;

import com.tubeplus.board_service.posting.domain.comment.CommentViewInfo;
import lombok.Builder;
import lombok.Data;

public interface CommentUseCase {

    long writeComment(PostCommentForm form);

    @Data
    @Builder
    class PostCommentForm {
        private final Long postingId;
        private final Long parentId;
        private final String commenterUuid;
        private final String contents;
    }


    CommentViewInfo readComment(ReadCommentDto dto);

    @Data
    @Builder
    class ReadCommentDto {
        private final long postingId;
        private final Long parentId;
    }



    CommentViewInfo modifyComment(Long idToModify, String contents);


    void deleteComment(long idToDelete);
}
