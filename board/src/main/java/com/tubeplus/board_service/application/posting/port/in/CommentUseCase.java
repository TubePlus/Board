package com.tubeplus.board_service.application.posting.port.in;

import com.tubeplus.board_service.application.posting.domain.comment.Comment;
import lombok.Builder;
import lombok.Data;

import java.util.List;

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


    List<Comment> readComments(ReadCommentsInfo readInfo);

    @Data(staticConstructor = "of")
    class ReadCommentsInfo {
        private final long postingId;
        private final Long parentCommentId;
    }



    Comment.CommentViewInfo modifyComment(Long idToModify, String contents);


    void deleteComment(long idToDelete);
}
