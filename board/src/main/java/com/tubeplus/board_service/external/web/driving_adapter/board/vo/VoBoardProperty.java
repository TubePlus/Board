package com.tubeplus.board_service.external.web.driving_adapter.board.vo;

import com.tubeplus.board_service.domain.board.model.Board;
import com.tubeplus.board_service.domain.board.model.BoardType;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.time.LocalDateTime;


@Slf4j
@Builder
@Data
public class VoBoardProperty implements Serializable {
    private final String boardName;
    private final BoardType boardType;
    private final String boardDescription;
    private final boolean visible;
    private final LocalDateTime limitDateTime;
    private final boolean erase;

    public static VoBoardProperty builtFrom(Board board) {
        log.info(board.toString());

        VoBoardProperty boardProperty = VoBoardProperty.builder()
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
}
