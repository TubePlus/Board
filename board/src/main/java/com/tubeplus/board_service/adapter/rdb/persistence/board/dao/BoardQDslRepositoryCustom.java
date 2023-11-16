package com.tubeplus.board_service.adapter.rdb.persistence.board.dao;

import com.tubeplus.board_service.application.board.port.out.BoardPersistable;
import com.tubeplus.board_service.adapter.rdb.persistence.board.BoardEntity;

import java.util.List;

public interface BoardQDslRepositoryCustom {

    List<BoardEntity> findBoards(BoardPersistable.FindBoardListDto findDto);

    Boolean updateCommonProperty(BoardPersistable.UpdateCommonPropertyDto dto);

    boolean softDeleteBoard(Long boardId);
}
