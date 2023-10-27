package com.tubeplus.board_service.external.rdb.driven_adapter.dao;

import com.tubeplus.board_service.domain.board.port.out.BoardPersistent;
import com.tubeplus.board_service.external.rdb.entity.BoardEntity;

import java.util.List;

public interface BoardQDslRepositoryCustom {

    List<BoardEntity> findBoards(BoardPersistent.ListFindDto findDto);

    Boolean updateBoard(BoardPersistent.UpdateDto dto);

    void softDeleteBoard(Long boardId);
}
