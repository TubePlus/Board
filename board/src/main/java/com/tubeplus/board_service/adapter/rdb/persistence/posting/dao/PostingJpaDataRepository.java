package com.tubeplus.board_service.adapter.rdb.persistence.posting.dao;

import com.tubeplus.board_service.adapter.rdb.persistence.posting.PostingEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostingJpaDataRepository extends JpaRepository<PostingEntity, Long> {

}