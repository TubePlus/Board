package com.tubeplus.board_service.adapter.rdb.posting.dao;


import com.tubeplus.board_service.adapter.rdb.posting.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentJpaDataRepository extends JpaRepository<CommentEntity, Long> {

    List<CommentEntity> findByPostingIdAndParentComment(long postingId, CommentEntity parentComment);

    Optional<CommentEntity> findFirstByParentComment(CommentEntity entity);
}
