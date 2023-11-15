package com.tubeplus.board_service.adapter.rdb.persistence.posting.dao;


import com.tubeplus.board_service.adapter.rdb.persistence.posting.CommentEntity;
import com.tubeplus.board_service.application.posting.port.out.CommentPersistable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface CommentJpaDataRepository extends JpaRepository<CommentEntity, Long> {

    List<CommentEntity> findByPostingIdAndParentComment(long postingId, CommentEntity parentComment);

}
