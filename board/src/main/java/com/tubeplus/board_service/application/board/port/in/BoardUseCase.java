package com.tubeplus.board_service.application.board.port.in;

import com.tubeplus.board_service.application.board.domain.Board;
import com.tubeplus.board_service.application.board.domain.BoardType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


public interface BoardUseCase {

    // Creator only
    @Data
    @Builder
    class CreateBoardForm {

        private final Long communityId;
        private final String boardName;
        private final BoardType boardType;
        private final String boardDescription;
        private final LocalDateTime limitDateTime;
    }

    Board createBoard(CreateBoardForm form);


    // Member only
    List<Board> listCommuBoards(BoardListInfo boardListInfo);

    @Data
    @Builder
    class BoardListInfo {

        private final Long communityId;
        private final Boolean visible;
        private final Boolean softDelete;
        private final String nameToSearch;
    }


    Board findBoard(Long boardId);


    void updateBoardProperty(Long boardId, BoardProperty updateInfo);

    @Data(staticConstructor = "of")
    class BoardProperty { //todo Board도메인과 합칠것

        private final BoardCommonProperty commonProperty;
        private final TimeLimitBoardProperty timeLimitProperty;

        public static BoardProperty of(Board board) {
            return BoardProperty.of(
                    BoardCommonProperty.builtFrom(board),
                    TimeLimitBoardProperty.of(board.getLimitDateTime())
            );
        }


        @Data
        @Builder
        public static class BoardCommonProperty {
            private final String boardName;
            private final BoardType boardType;
            private final String boardDescription;
            private final Boolean visible;
            private final Boolean softDelete;

            public static BoardCommonProperty builtFrom(Board board) {
                return BoardCommonProperty.builder()
                        .boardName(board.getBoardName())
                        .boardType(board.getBoardType())
                        .boardDescription(board.getBoardDescription())
                        .visible(board.isVisible())
                        .softDelete(board.isSoftDelete())
                        .build();
            }
        }

        @Data(staticConstructor = "of")
        public static class TimeLimitBoardProperty {
            private final LocalDateTime limitDateTime;
        }
    }


    void completelyDeleteBoard(Long boardId);


    void softlyDeleteBoard(Long boardId);

}
