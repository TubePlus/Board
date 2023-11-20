package com.tubeplus.board_service.application.posting.service;

import com.tubeplus.board_service.adapter.web.error.BusinessException;
import com.tubeplus.board_service.adapter.web.error.ErrorCode;
import com.tubeplus.board_service.application.posting.domain.comment.Comment;
import com.tubeplus.board_service.application.posting.domain.comment.Comment.CommentViewInfo;
import com.tubeplus.board_service.application.posting.port.in.PostingCommentUseCase;
import com.tubeplus.board_service.application.posting.port.in.WebCommentUseCase;
import com.tubeplus.board_service.application.posting.port.out.CommentPersistable;
import com.tubeplus.board_service.application.posting.port.out.CommentPersistable.FindCommentDto;
import com.tubeplus.board_service.application.posting.port.out.CommentPersistable.SaveCommentDto;
import com.tubeplus.board_service.application.posting.port.out.CommentPersistable.UpdateCommentDto;
import com.tubeplus.board_service.application.posting.port.out.PostingPersistable;
import com.tubeplus.board_service.global.Exceptionable;
import com.tubeplus.board_service.global.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service("commentService")
@RequiredArgsConstructor
public class CommentService implements WebCommentUseCase, PostingCommentUseCase {

    private final CommentPersistable commentPersistence;
    private final PostingPersistable postingPersistence;
    private final KafkaProducer kafkaProducer;

    @Override
    public final Comment writeComment(PostCommentForm form) {

        SaveCommentDto dto
                = SaveCommentDto.builtFrom(form);

        Comment savedComment
                = commentPersistence.saveComment(dto)
                .ifExceptioned.thenThrow(ErrorCode.SAVE_ENTITY_FAILED);

       Long communityId = postingPersistence.getPostingCommuId(savedComment.getPostingId())
               .ifExceptioned.thenThrow(ErrorCode.FIND_ENTITY_FAILED);

        // todo : 내 댓글에 대댓글 짜이면 알람 보내기 -> etc parentdId 의 commetUuid를 확인해서 알람 commentAlarm
        try{
            kafkaProducer.producerPutComment(communityId, 1L);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        UpdateCommentDto dto = UpdateCommentDto.of(idToModify, contents);

        Comment modifiedComment
                = commentPersistence.updateComment(dto)
                .ifExceptioned.thenThrow(ErrorCode.UPDATE_ENTITY_FAILED);

        return modifiedComment.getViewInfo();
    }

    @Override
    public void deleteComment(long idToDelete) {

        Boolean deleted
                = commentPersistence.deleteComment(idToDelete)
                .ifExceptioned.thenThrow(ErrorCode.DELETE_ENTITY_FAILED);

        if (!deleted)
            throw new BusinessException(ErrorCode.DELETE_ENTITY_FAILED);
    }

}
