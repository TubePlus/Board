package com.tubeplus.board_service.adapter.rdb.persistence.posting.dao;

import com.tubeplus.board_service.adapter.rdb.persistence.posting.PostingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface PostingJpaDataRepository extends JpaRepository<PostingEntity, Long> {

    Optional<Long> findOneIdByIdLessThanOrderByIdDesc(Long lastCursoredId);
}