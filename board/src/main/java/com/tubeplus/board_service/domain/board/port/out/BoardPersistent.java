package com.tubeplus.board_service.domain.board.port.out;

import com.tubeplus.board_service.domain.board.model.Board;
import com.tubeplus.board_service.domain.board.model.BoardType;
import com.tubeplus.board_service.domain.board.port.in.BoardUseCase;
import com.tubeplus.board_service.global.Exceptionable;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


public interface BoardPersistent {

    Exceptionable<Board, SaveDto> saveBoard(SaveDto data);

    @Data
    @Builder
    class SaveDto {
        private final Long communityId;
        private final String boardName;
        private final BoardType boardType;
        private final String boardDescription;
        private final boolean visible;
        private final LocalDateTime limitDateTime;

        public static SaveDto of(BoardUseCase.FormToMakeBoard form) {
            return new SaveDto(
                    form.getCommunityId(),
                    form.getBoardName(),
                    form.getBoardType(),
                    form.getBoardDescription(),
                    true,
                    form.getLimitDateTime()
            );
        }
    }


    Exceptionable<List<Board>, FindDto> findBoards(BoardPersistent.FindDto findDto);

    @Data
    @Builder
    class FindDto {
        private final Long communityId;
        private final Boolean visible;
        private final Boolean erase;
        private final String nameToSearch;

        public static FindDto of(BoardUseCase.BoardsFindInfo findInfo) {
            return new FindDto(
                    findInfo.getCommunityId(),
                    findInfo.getVisible(),
                    findInfo.getErase(),
                    findInfo.getNameToSearch()
            );
        }
    }

}
