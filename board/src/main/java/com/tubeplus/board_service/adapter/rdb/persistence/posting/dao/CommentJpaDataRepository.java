package com.tubeplus.board_service.adapter.rdb.persistence.posting.dao;


import com.tubeplus.board_service.adapter.rdb.persistence.posting.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentJpaDataRepository extends JpaRepository<CommentEntity, Long> {
}
