package com.tubeplus.board_service.adapter.rdb.persistence.posting;

import com.tubeplus.board_service.adapter.rdb.persistence.posting.dao.CommentJpaDataRepository;
import com.tubeplus.board_service.application.posting.port.out.CommentPersistent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.Opt;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Slf4j
@RequiredArgsConstructor

@Component("commentPersistence")
public class CommentPersistence implements CommentPersistent {

    private final CommentJpaDataRepository jpaDataRepo;

    @Override
    public Long saveComment(SaveCommentDto dto) {

        CommentEntity parentComment = null;

        if (dto.getParentId() != null) {
            parentComment
                    = jpaDataRepo.findById(dto.getParentId())
                    .orElseThrow(() -> new RuntimeException("부모 댓글이 존재하지 않습니다."));

            if (parentComment.getPostingId() != dto.getPostingId()) {
                throw new RuntimeException("부모 댓글의 게시글과 현재 게시글이 다릅니다.");
            }
        }

        CommentEntity savedEntity
                = jpaDataRepo.save(CommentEntity.builtFrom(dto, parentComment));

        return savedEntity.getId();
    }


}
