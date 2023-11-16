package com.tubeplus.board_service.adapter.rdb.persistence.posting;

import com.tubeplus.board_service.adapter.rdb.persistence.posting.dao.CommentJpaDataRepository;
import com.tubeplus.board_service.application.posting.domain.comment.Comment;
import com.tubeplus.board_service.application.posting.port.out.CommentPersistable;
import com.tubeplus.board_service.global.Exceptionable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;


@Slf4j
@RequiredArgsConstructor

@Component("commentPersistence")
public class CommentPersistence implements CommentPersistable {

    private final CommentJpaDataRepository jpaDataRepo;

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


    protected Comment entityToDomain(CommentEntity entity) {

        return Comment.of(
                entity.getId(),
                Comment.CommentViewInfo.builder()
                        .postingId(entity.getPostingId())
                        .parentId(entity.getParentComment() == null ? null : entity.getParentComment().getId())
                        .hasChild(entity.getParentComment() == null && jpaDataRepo.findFirstByParentComment(entity).isPresent())
                        .contents(entity.getContents())
                        .commenterUuid(entity.getCommenterUuid())
                        .build()
        );
    }


}
