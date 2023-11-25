package com.tubeplus.board_service.adapter.rdb.posting;

import com.tubeplus.board_service.adapter.rdb.posting.dao.CommentJpaDataRepository;
import com.tubeplus.board_service.adapter.rdb.posting.dao.PostingJpaDataRepository;
import com.tubeplus.board_service.adapter.rdb.posting.entity.CommentEntity;
import com.tubeplus.board_service.adapter.rdb.posting.entity.PostingEntity;
import com.tubeplus.board_service.application.posting.domain.comment.Comment;
import com.tubeplus.board_service.application.posting.domain.comment.Comment.CommentViewInfo;
import com.tubeplus.board_service.application.posting.domain.posting.Posting;
import com.tubeplus.board_service.application.posting.port.out.CommentPersistable;
import com.tubeplus.board_service.global.Exceptionable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@RequiredArgsConstructor

@Component
public class CommentPersistence implements CommentPersistable {

    private final CommentJpaDataRepository commentJpaDataRepo;
    private final PostingJpaDataRepository postingJpaDataRepo;

    @Override
    public Exceptionable<Boolean, Long> deleteComment(Long idToDelete) {

        return Exceptionable.act(id -> {

            commentJpaDataRepo.deleteById(id);

            return commentJpaDataRepo.findById(id).isEmpty();

        }, idToDelete);

    }

    @Override
    public Exceptionable<Long, Long> countComments(long postingId) {

        return Exceptionable.act(commentJpaDataRepo::countByPostingId, postingId);
    }


    @Override
    public Exceptionable<Comment, SaveCommentDto> saveComment(SaveCommentDto saveCommentDto) {

        return Exceptionable.act(dto ->
        {

            /**/
            CommentEntity entityToSave;

            CommentEntity parentComment =
                    dto.getParentId() == null
                            ? parentComment = null
                            : getValidParent(dto.getParentId(), dto.getPostingId());

            PostingEntity commentedPosting
                    = postingJpaDataRepo.findById(dto.getPostingId())
                    .orElseThrow(() -> new RuntimeException("posting is not found."));

            entityToSave
                    = CommentEntity.builtFrom(dto, commentedPosting, parentComment);

            /**/
            CommentEntity savedEntity
                    = commentJpaDataRepo.save(entityToSave);

            return entityToDomain(savedEntity);

        }, saveCommentDto);

    }

    @Transactional(readOnly = true)
    protected final CommentEntity getValidParent(Long parentId, long postingId) {

        CommentEntity parentComment
                = commentJpaDataRepo.findById(parentId)
                .orElseThrow(() -> new RuntimeException("parent comment is not found."));

        if (parentComment.getPosting().getId() != postingId) {
            throw new RuntimeException("postingId between parent and child is not matched.");
        }
        return parentComment;
    }


    @Override
    public Exceptionable<List<Comment>, FindCommentDto> findComments(FindCommentDto findDto) {

        return Exceptionable.act(dto -> {

            CommentEntity parentComment
                    =
                    dto.isFindingChildren()
                            ? getValidParent(dto.getParentId(), dto.getPostingId())
                            : null;

            List<CommentEntity> foundEntities
                    = commentJpaDataRepo.findByPostingIdAndParentComment(
                    dto.getPostingId(), parentComment);

            List<Comment> foundComments
                    = foundEntities
                    .stream()
                    .map(this::entityToDomain)
                    .toList();

            return foundComments;

        }, findDto);
    }

    @Override
    public Exceptionable<Comment, UpdateCommentDto> updateComment(UpdateCommentDto updateDto) {

        return Exceptionable.act(dto -> {

            CommentEntity entityToUpdate
                    = commentJpaDataRepo.findById(dto.getIdToModify())
                    .orElseThrow(() -> new RuntimeException("comment is not found."));

            entityToUpdate.setContent(dto.getContent());

            CommentEntity updatedEntity
                    = commentJpaDataRepo.save(entityToUpdate);

            return entityToDomain(updatedEntity);

        }, updateDto);
    }


    protected Comment entityToDomain(CommentEntity entity) {

        Long parentId =
                entity.getParentComment() == null
                        ? null
                        : entity.getParentComment().getId();


        boolean hasChild
                = entity.getParentComment() == null
                && commentJpaDataRepo.findFirstByParentComment(entity).isPresent();


        return Comment.of(
                entity.getId(),
                entity.getPosting().getId(),
                CommentViewInfo.builder()
                        .parentId(parentId)
                        .hasChild(hasChild)
                        .content(entity.getContent())
                        .commenterUuid(entity.getCommenterUuid())
                        .build()
        );
    }


}
