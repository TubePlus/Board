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
    class MakeBoardForm {

        private final Long communityId;
        private final String boardName;
        private final BoardType boardType;
        private final String boardDescription;
        private final LocalDateTime limitDateTime;
    }

    Board makeBoard(MakeBoardForm form);


    // Member only
    List<Board> listCommuBoards(BoardListInfo boardListInfo);

    @Data
    @Builder
    class BoardListInfo {

        private final Long communityId;
        private final Boolean visible;
        private final Boolean erase;
        private final String nameToSearch;
    }


    Board findBoard(Long boardId);


    void updateBoardCommonProperty(Long boardId, BoardProperty.Common commonProperty);

    @Data(staticConstructor = "of")
    class BoardProperty {

        private final Common common;
        private final LocalDateTime limitDateTime;

        @Data
        @Builder
        public static class Common {
            private final String boardName;
            private final BoardType boardType;
            private final String boardDescription;
            private final Boolean visible;
            private final Boolean softDelete;
        }
    }


    void completelyDeleteBoard(Long boardId);


    void softlyDeleteBoard(Long boardId);

}
