package com.tubeplus.board_service.adapter.rdb.posting.dao;

import com.tubeplus.board_service.adapter.rdb.posting.entity.PostingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface PostingJpaDataRepository extends JpaRepository<PostingEntity, Long> {

    Optional<Long> findOneIdByIdLessThanOrderByIdDesc(Long lastCursoredId);
}