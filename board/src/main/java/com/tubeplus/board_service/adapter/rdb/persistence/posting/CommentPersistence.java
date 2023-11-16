package com.tubeplus.board_service.adapter.rdb.persistence.posting;

import com.tubeplus.board_service.adapter.rdb.persistence.posting.dao.CommentJpaDataRepository;
import com.tubeplus.board_service.application.posting.domain.comment.Comment;
import com.tubeplus.board_service.application.posting.domain.comment.Comment.CommentViewInfo;
import com.tubeplus.board_service.application.posting.port.out.CommentPersistable;
import com.tubeplus.board_service.global.Exceptionable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;


@Slf4j
@RequiredArgsConstructor

@Component("commentPersistence")
public class CommentPersistence implements CommentPersistable {

    private final CommentJpaDataRepository jpaDataRepo;

    @Override
    public Exceptionable<Boolean, Long> deleteComment(Long idToDelete) {

        return Exceptionable.act(id -> {

            jpaDataRepo.deleteById(id);

            return jpaDataRepo.findById(id).isEmpty();

        }, idToDelete);

    }

    @Override
    public Exceptionable<Comment, SaveCommentDto> saveComment(SaveCommentDto saveCommentDto) {

        return Exceptionable.act(dto -> {

            CommentEntity parentComment = null;
            if (dto.getParentId() != null)
                parentComment = getValidParent(dto.getParentId(), dto.getPostingId());

            CommentEntity entityToSave
                    = CommentEntity.builtFrom(dto, parentComment);

            CommentEntity savedEntity
                    = jpaDataRepo.save(entityToSave);

            return entityToDomain(savedEntity);

        }, saveCommentDto);

    }

    protected final CommentEntity getValidParent(Long parentId, long postingId) {

        CommentEntity parentComment
                = jpaDataRepo.findById(parentId)
                .orElseThrow(() -> new RuntimeException("parent comment is not found."));

        if (parentComment.getPostingId() != postingId) {
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
                    = jpaDataRepo.findByPostingIdAndParentComment(
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
                    = jpaDataRepo.findById(dto.getIdToModify())
                    .orElseThrow(() -> new RuntimeException("comment is not found."));

            entityToUpdate.setContent(dto.getContent());

            CommentEntity updatedEntity
                    = jpaDataRepo.save(entityToUpdate);

            return entityToDomain(updatedEntity);

        }, updateDto);
    }


    protected Comment entityToDomain(CommentEntity entity) {

        return Comment.of(
                entity.getId(),
                entity.getPostingId(),
                CommentViewInfo.builder()
                        .parentId(
                                entity.getParentComment() == null
                                        ? null
                                        : entity.getParentComment().getId()
                        )
                        .hasChild(
                                entity.getParentComment() == null
                                        && jpaDataRepo.findFirstByParentComment(entity).isPresent()
                        )
                        .content(entity.getContent())
                        .commenterUuid(entity.getCommenterUuid())
                        .build()
        );
    }


}
