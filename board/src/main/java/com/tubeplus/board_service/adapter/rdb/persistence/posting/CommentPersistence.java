package com.tubeplus.board_service.adapter.rdb.persistence.posting;

import com.tubeplus.board_service.adapter.rdb.persistence.posting.dao.CommentJpaDataRepository;
import com.tubeplus.board_service.application.posting.domain.comment.Comment;
import com.tubeplus.board_service.application.posting.port.out.CommentPersistable;
import com.tubeplus.board_service.global.Exceptionable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;


@Slf4j
@RequiredArgsConstructor

@Component("commentPersistence")
public class CommentPersistence implements CommentPersistable {

    private final CommentJpaDataRepository jpaDataRepo;

    @Override
    public Exceptionable<Long, SaveCommentDto> saveComment(SaveCommentDto saveCommentDto) {

        Function<SaveCommentDto, Long> saveComment = dto -> {

            CommentEntity parentComment = null;
            if (dto.getParentId() != null)
                parentComment = getValidParent(dto.getParentId(), dto.getPostingId());

            CommentEntity entityToSave
                    = CommentEntity.builtFrom(dto, parentComment);

            CommentEntity savedEntity
                    = jpaDataRepo.save(entityToSave);

            return savedEntity.getId();
        };


        return Exceptionable.act(saveComment, saveCommentDto);
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

        return Exceptionable.act(
                dto -> {
                    CommentEntity parentComment
                            = dto.isFindingChildren()
                            ? getValidParent(dto.getParentId(), dto.getPostingId())
                            : null;

                    return jpaDataRepo.findByPostingIdAndParentComment
                                    (dto.getPostingId(), parentComment)
                            .stream().map(CommentEntity::toDomain).toList();
                }
                , findDto);
    }


}
