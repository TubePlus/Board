package com.tubeplus.board_service.application.posting.port.in;

import com.tubeplus.board_service.application.posting.domain.comment.Comment;
import lombok.Builder;
import lombok.Data;

public interface CommentUseCase {

    Long writeComment(PostCommentForm form);

    @Data
    @Builder
    class PostCommentForm {
        private final Long postingId;
        private final Long parentId;
        private final String commenterUuid;
        private final String contents;
    }


    Comment.CommentViewInfo readComment(ReadCommentDto dto);

    @Data
    @Builder
    class ReadCommentDto {
        private final long postingId;
        private final Long parentId;
    }



    Comment.CommentViewInfo modifyComment(Long idToModify, String contents);


    void deleteComment(long idToDelete);
}
