package com.tubeplus.board_service.application.posting.service;

import com.tubeplus.board_service.application.posting.domain.comment.Comment;
import com.tubeplus.board_service.application.posting.port.in.CommentUseCase;
import com.tubeplus.board_service.application.posting.port.out.CommentPersistent;
import com.tubeplus.board_service.application.posting.port.out.CommentPersistent.SaveCommentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("commentService")
@RequiredArgsConstructor
public class CommentService implements CommentUseCase {

    private final CommentPersistent commentPersistent;

    @Override
    public final Long writeComment(PostCommentForm form) {

        SaveCommentDto dto
                = SaveCommentDto.builtFrom(form);

        Long savedCommentId
                = commentPersistent.saveComment(dto);

        return savedCommentId;
    }

    @Override
    public Comment.CommentViewInfo readComment(ReadCommentDto dto) {
        return null;
    }

    @Override
    public Comment.CommentViewInfo modifyComment(Long idToModify, String contents) {
        return null;
    }

    @Override
    public void deleteComment(long idToDelete) {

    }

}
