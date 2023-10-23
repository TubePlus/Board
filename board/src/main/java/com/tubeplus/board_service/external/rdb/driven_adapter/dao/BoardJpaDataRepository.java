package com.tubeplus.board_service.external.rdb.driven_adapter.dao;

import com.tubeplus.board_service.external.rdb.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BoardJpaDataRepository extends JpaRepository<BoardEntity, Long> {

}