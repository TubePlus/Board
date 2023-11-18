package com.tubeplus.board_service.adapter.rdb.board.dao;

import com.tubeplus.board_service.adapter.rdb.board.BoardEntity;
import com.tubeplus.board_service.application.board.port.out.BoardPersistable;

import java.util.List;

public interface BoardQDslRepositoryCustom {

    List<BoardEntity> findBoards(BoardPersistable.FindBoardListDto findDto);

    Boolean updateCommonProperty(BoardPersistable.UpdateCommonPropertyDto dto);

    boolean softDeleteBoard(Long boardId);
}
