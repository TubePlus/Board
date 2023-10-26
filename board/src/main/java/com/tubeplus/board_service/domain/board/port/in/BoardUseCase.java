package com.tubeplus.board_service.domain.board.port.in;

import com.tubeplus.board_service.domain.board.model.Board;
import com.tubeplus.board_service.domain.board.model.BoardType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


public interface BoardUseCase {

    // Creator only
    Board makeBoard(FormToMakeBoard form);

    @Data
    @Builder
    class FormToMakeBoard {
        private final Long communityId;
        private final String boardName;
        private final BoardType boardType;
        private final String boardDescription;
        private final LocalDateTime limitDateTime;
    }


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


    void updateBoardProperty(Long boardId, BoardProperty property);

    @Data
    @Builder
    class BoardProperty {
        private final String boardName;
        private final BoardType boardType;
        private final String boardDescription;
        private final Boolean visible;
        private final LocalDateTime limitDateTime;
        private final Boolean erase;
    }
}
