package com.tubeplus.board_service.adapter.rdb.board.dao;

import com.tubeplus.board_service.adapter.rdb.board.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BoardJpaDataRepository extends JpaRepository<BoardEntity, Long> {

}