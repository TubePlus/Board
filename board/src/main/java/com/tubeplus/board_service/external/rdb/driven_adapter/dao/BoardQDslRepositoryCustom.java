package com.tubeplus.board_service.external.rdb.driven_adapter.dao;

import com.tubeplus.board_service.domain.board.port.out.BoardPersistent;
import com.tubeplus.board_service.external.rdb.entity.BoardEntity;

import java.util.List;

public interface BoardQDslRepositoryCustom {

    public List<BoardEntity> findBoards(BoardPersistent.FindListDto findDto);

}
