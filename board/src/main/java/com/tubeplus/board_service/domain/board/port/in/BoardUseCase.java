package com.tubeplus.board_service.domain.board.port.in;

import com.tubeplus.board_service.domain.board.model.Board;
import com.tubeplus.board_service.domain.board.model.BoardType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


public interface BoardUseCase {

    // Creator only
    @Data
    @Builder
    class FormToMakeBoard {

        private final Long communityId;
        private final String boardName;
        private final BoardType boardType;
        private final String boardDescription;
        private final LocalDateTime limitDateTime;
    }

    Board makeBoard(FormToMakeBoard form);


    // Member only
    @Data
    @Builder
    class BoardListInfo {

        private final Long communityId;
        private final Boolean visible;
        private final Boolean erase;
        private final String nameToSearch;
    }

    List<Board> listCommuBoards(BoardListInfo boardListInfo);


    Board findBoard(Long boardId);


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

    void updateBoardProperty(Long boardId, BoardProperty property);


    void completelyDeleteBoard(Long boardId);


    void softlyDeleteBoard(Long boardId);

}
