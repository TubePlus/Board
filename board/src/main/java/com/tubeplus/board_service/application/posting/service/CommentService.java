package com.tubeplus.board_service.application.posting.service;

import com.tubeplus.board_service.application.posting.domain.comment.CommentViewInfo;
import com.tubeplus.board_service.application.posting.port.in.CommentUseCase;
import org.springframework.stereotype.Service;


@Service
public class CommentService implements CommentUseCase {

    @Override
    public long writeComment(PostCommentForm form) {
        return 0;
    }

    @Override
    public CommentViewInfo readComment(ReadCommentDto dto) {
        return null;
    }

    @Override
    public CommentViewInfo modifyComment(Long idToModify, String contents) {
        return null;
    }

    @Override
    public void deleteComment(long idToDelete) {

    }
}
