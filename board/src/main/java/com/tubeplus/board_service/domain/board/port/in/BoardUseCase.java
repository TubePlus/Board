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
        private final LocalDateTime limitDateTime ;
    }


    // Member only
    List<Board> findCommuBoards(BoardsFindInfo findInfo);

    @Data
    @Builder
    class BoardsFindInfo {
        private final Long communityId;
        private final Boolean visible;
        private final Boolean erase;
        private final String nameToSearch;
    }


    Board findBoard(Long boardId);

}
