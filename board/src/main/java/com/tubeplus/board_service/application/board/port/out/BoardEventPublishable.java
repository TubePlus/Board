package com.tubeplus.board_service.application.board.port.out;

import com.tubeplus.board_service.application.board.domain.Board;

public interface BoardEventPublishable {

    void publishBoardCreated(Board createdBoard);
}
