package com.tubeplus.board_service.domain.board.port.out;

import com.tubeplus.board_service.domain.board.model.Board;
import com.tubeplus.board_service.domain.board.model.BoardType;
import com.tubeplus.board_service.domain.board.port.in.BoardUseCase;
import com.tubeplus.board_service.global.Exceptionable;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;


public interface BoardPersistent {

    Exceptionable<Board, SaveDto> saveBoard(SaveDto data);

    //todo of -> builtFrom 빌더 사용
    @Data
    @Builder
    @Slf4j
    class SaveDto {
        private final Long communityId;
        private final String boardName;
        private final BoardType boardType;
        private final String boardDescription;
        private final boolean visible;
        private final LocalDateTime limitDateTime;

        public static SaveDto of(BoardUseCase.FormToMakeBoard form) {
            log.info(form.toString());

            SaveDto saveDto = new SaveDto(
                    form.getCommunityId(),
                    form.getBoardName(),
                    form.getBoardType(),
                    form.getBoardDescription(),
                    true,
                    form.getLimitDateTime()
            );
            log.info(saveDto.toString());

            return saveDto;
        }
    }


    Exceptionable<List<Board>, FindListDto> findBoardList(FindListDto findDto);

    @Data
    @Slf4j
    @Builder
    class FindListDto {
        private final Long communityId;
        private final Boolean visible;
        private final Boolean erase;
        private final String nameToSearch;

        public static FindListDto of(BoardUseCase.BoardsFindInfo findInfo) {
            log.info(findInfo.toString());

            FindListDto findListDto = new FindListDto(
                    findInfo.getCommunityId(),
                    findInfo.getVisible(),
                    findInfo.getErase(),
                    findInfo.getNameToSearch()
            );
            log.info(findInfo.toString());

            return findListDto;
        }
    }


    Exceptionable<Board, Long> findBoard(Long boardId);

}
