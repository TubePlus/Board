package com.tubeplus.board_service.application.posting.service;

import com.tubeplus.board_service.adapter.web.error.BusinessException;
import com.tubeplus.board_service.adapter.web.error.ErrorCode;
import com.tubeplus.board_service.application.posting.domain.comment.Comment;
import com.tubeplus.board_service.application.posting.domain.comment.Comment.CommentViewInfo;
import com.tubeplus.board_service.application.posting.port.in.CommentUseCase;
import com.tubeplus.board_service.application.posting.port.out.CommentPersistable;
import com.tubeplus.board_service.application.posting.port.out.CommentPersistable.FindCommentDto;
import com.tubeplus.board_service.application.posting.port.out.CommentPersistable.SaveCommentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service("commentService")
@RequiredArgsConstructor
public class CommentService implements CommentUseCase {

    private final CommentPersistable commentPersistence;

    @Override
    public final Comment writeComment(PostCommentForm form) {

        SaveCommentDto dto
                = SaveCommentDto.builtFrom(form);

        Comment savedComment
                = commentPersistence.saveComment(dto)
                .ifExceptioned.thenThrow(ErrorCode.SAVE_ENTITY_FAILED);

        // todo : 내 댓글에 대댓글 짜이면 알람 보내기 -> etc parentdId 의 commetUuid를 확인해서 알람 commentAlarm
        // todo : postingId를 통해서 boardId를 알아내고 -> boardId로 communityId를 알아내서 addComment 토픽

        return savedComment;
    }

    @Override
    public List<Comment> readComments(ReadCommentsInfo readInfo) {

        FindCommentDto dto = FindCommentDto.of(readInfo);

        List<Comment> comments
                = commentPersistence.findComments(dto)
                .ifExceptioned.thenThrow(ErrorCode.FIND_ENTITY_FAILED);

        if (comments.isEmpty())
            throw new BusinessException(ErrorCode.NOT_FOUND_RESOURCE);

        return comments;
    }

    @Override
    public CommentViewInfo modifyComment(Long idToModify, String contents) {
        return null;
    }

    @Override
    public void deleteComment(long idToDelete) {

    }

}
