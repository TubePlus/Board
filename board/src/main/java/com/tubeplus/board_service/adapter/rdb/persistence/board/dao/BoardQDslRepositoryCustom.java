package com.tubeplus.board_service.adapter.rdb.persistence.board.dao;

import com.tubeplus.board_service.application.board.port.out.BoardPersistent;
import com.tubeplus.board_service.adapter.rdb.persistence.board.BoardEntity;

import java.util.List;

public interface BoardQDslRepositoryCustom {

    List<BoardEntity> findBoards(BoardPersistent.ListFindDto findDto);

    Boolean updateBoard(BoardPersistent.UpdateDto dto);

    boolean softDeleteBoard(Long boardId);
}
