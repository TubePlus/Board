package com.tubeplus.board_service.board.port.out;

import com.tubeplus.board_service.board.model.Board;
import com.tubeplus.board_service.board.model.BoardType;
import com.tubeplus.board_service.board.port.in.BoardUseCase;
import com.tubeplus.board_service.global.Exceptionable;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;


//todo of -> builtFrom 빌더 사용
public interface BoardPersistent {


    Exceptionable<Board, SaveDto> saveBoard(SaveDto dto);

    @Data
    @Slf4j
    @Builder
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


    Exceptionable<List<Board>, ListFindDto> findBoardList(ListFindDto Dto);

    @Data
    @Slf4j
    @Builder
    class ListFindDto {

        private final Long communityId;
        private final Boolean visible;
        private final Boolean erase;
        private final String nameToSearch;

        public static ListFindDto of(BoardUseCase.BoardListInfo findInfo) {
            log.info(findInfo.toString());

            ListFindDto findListDto = new ListFindDto(
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


    Exceptionable<Boolean, UpdateDto> updateBoard(UpdateDto dto);

    @Data
    @Slf4j
    @Builder
    class UpdateDto {

        private final Long id;
        private final String boardName;
        private final BoardType boardType;
        private final String boardDescription;
        private final Boolean visible;
        private final LocalDateTime limitDateTime;
        private final Boolean erase;

        public static UpdateDto builtFrom(Long boardId, BoardUseCase.BoardProperty p) {
            log.info(p.toString());

            UpdateDto dto = UpdateDto.builder()
                    .id(boardId)
                    .boardName(p.getBoardName())
                    .boardType(p.getBoardType())
                    .boardDescription(p.getBoardDescription())
                    .visible(p.getVisible())
                    .limitDateTime(p.getLimitDateTime())
                    .erase(p.getErase())
                    .build();
            log.info(dto.toString());

            return dto;
        }

    }


    Exceptionable<Boolean, Long> completelyDeleteBoard(Long boardId);


    Exceptionable<Boolean, Long> softlyDeleteBoard(Long boardId);

}
