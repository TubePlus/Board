package com.tubeplus.board_service.adapter.web.controller.board.vo;

import com.tubeplus.board_service.application.board.domain.Board;
import com.tubeplus.board_service.application.board.domain.BoardType;
import com.tubeplus.board_service.application.board.port.in.BoardUseCase;
import com.tubeplus.board_service.application.board.port.in.BoardUseCase.BoardProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data(staticConstructor = "of")
@Slf4j
public class VoBoardProperty implements Serializable {


    private final VoBoardProperty.Common common;
    private final LocalDateTime limitDateTime;


    @Data
    @Builder
    @Slf4j
    public static class Common {
        private final String boardName;
        private final BoardType boardType;
        private final String boardDescription;
        private final Boolean visible;
        private final Boolean softDelete;

        public BoardProperty.Common buildDomain() {
            return BoardProperty.Common.builder().boardName(boardName).boardType(boardType).boardDescription(boardDescription).visible(visible).softDelete(softDelete).build();
        }

        public static VoBoardProperty.Common builtFrom(Board board) {
            log.info(board.toString());

            VoBoardProperty.Common commonProperty = VoBoardProperty.Common.builder().boardName(board.getBoardName()).boardType(board.getBoardType()).boardDescription(board.getBoardDescription()).visible(board.isVisible()).softDelete(board.isSoftDeleted()).build();
            log.info(commonProperty.toString());

            return commonProperty;
        }
    }

    public static VoBoardProperty of(Board board) {

        return new VoBoardProperty(VoBoardProperty.Common.builtFrom(board), board.getLimitDateTime());
    }

    public BoardUseCase.BoardProperty buildDomain() {
        log.info(this.toString());

        BoardProperty.Common commonProperty = BoardProperty.Common.builder().boardName(common.boardName).boardType(common.boardType).boardDescription(common.boardDescription).visible(common.visible).softDelete(common.softDelete).build();

        log.info(commonProperty.toString());

        return BoardProperty.of(commonProperty, limitDateTime);
    }


}
