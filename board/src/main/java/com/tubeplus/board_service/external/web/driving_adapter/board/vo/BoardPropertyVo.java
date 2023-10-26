package com.tubeplus.board_service.external.web.driving_adapter.board.vo;

import com.tubeplus.board_service.domain.board.model.Board;
import com.tubeplus.board_service.domain.board.model.BoardType;
import com.tubeplus.board_service.domain.board.port.in.BoardUseCase;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@Slf4j
@Builder
public class BoardPropertyVo implements Serializable {

    private final String boardName;
    private final BoardType boardType;
    private final String boardDescription;
    private final Boolean visible;
    private final LocalDateTime limitDateTime;
    private final Boolean erase;


    public static BoardPropertyVo builtFrom(Board board) {
        log.info(board.toString());

        BoardPropertyVo boardProperty = BoardPropertyVo.builder()
                .boardName(board.getBoardName())
                .boardType(board.getBoardType())
                .boardDescription(board.getBoardDescription())
                .visible(board.isVisible())
                .limitDateTime(board.getLimitDateTime())
                .erase(board.isErase())
                .build();
        log.info(boardProperty.toString());

        return boardProperty;
    }

    public BoardUseCase.BoardProperty buildBoardProperty() {
        log.info(this.toString());

        BoardUseCase.BoardProperty builtBoardProperty
                = BoardUseCase.BoardProperty.builder()
                .boardName(boardName)
                .boardType(boardType)
                .boardDescription(boardDescription)
                .visible(visible)
                .limitDateTime(limitDateTime)
                .erase(erase)
                .build();
        log.info(builtBoardProperty.toString());

        return builtBoardProperty;
    }

}
