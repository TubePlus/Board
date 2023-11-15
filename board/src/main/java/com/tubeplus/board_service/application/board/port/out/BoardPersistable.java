package com.tubeplus.board_service.application.board.port.out;

import com.tubeplus.board_service.application.board.domain.Board;
import com.tubeplus.board_service.application.board.domain.BoardType;
import com.tubeplus.board_service.application.board.port.in.BoardUseCase;
import com.tubeplus.board_service.application.board.port.in.BoardUseCase.BoardProperty.TimeLimitBoardProperty;
import com.tubeplus.board_service.global.Exceptionable;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;


public interface BoardPersistable {


    Exceptionable<Board, SaveBoardDto> saveBoard(SaveBoardDto dto);


    @Data
    @Slf4j
    @Builder
    class SaveBoardDto {
        private final Long communityId;
        private final String boardName;
        private final BoardType boardType;
        private final String boardDescription;
        private final boolean visible;
        private final LocalDateTime limitDateTime;

        public static SaveBoardDto of(BoardUseCase.MakeBoardForm form) {
            log.info(form.toString());

            SaveBoardDto saveDto = new SaveBoardDto(
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


    Exceptionable<List<Board>, FindBoardListDto> findBoardList(FindBoardListDto Dto);

    @Data
    @Slf4j
    @Builder
    class FindBoardListDto {
        private final Long communityId;
        private final Boolean visible;
        private final Boolean softDelete;
        private final String nameToSearch;

        public static FindBoardListDto of(BoardUseCase.BoardListInfo findInfo) {
            log.info(findInfo.toString());

            FindBoardListDto findListDto = new FindBoardListDto(
                    findInfo.getCommunityId(),
                    findInfo.getVisible(),
                    findInfo.getSoftDelete(),
                    findInfo.getNameToSearch()
            );
            log.info(findInfo.toString());

            return findListDto;
        }
    }


    Exceptionable<Board, Long> findBoard(Long boardId);


    Exceptionable<Boolean, UpdateCommonPropertyDto> updateCommonProperty(UpdateCommonPropertyDto dto);

    @Data
    @Slf4j
    @Builder
    class UpdateCommonPropertyDto {

        private final Long id;
        private final String boardName;
        private final BoardType boardType;
        private final String boardDescription;
        private final Boolean visible;
        private final Boolean softDelete;

        public static UpdateCommonPropertyDto builtFrom(Long boardId, BoardUseCase.BoardProperty.BoardCommonProperty p) {
            log.info(p.toString());

            UpdateCommonPropertyDto dto = UpdateCommonPropertyDto.builder()
                    .id(boardId)
                    .boardName(p.getBoardName())
                    .boardType(p.getBoardType())
                    .boardDescription(p.getBoardDescription())
                    .visible(p.getVisible())
                    .softDelete(p.getSoftDelete())
                    .build();
            log.info(dto.toString());

            return dto;
        }
    }


    Exceptionable<Boolean, UpdateTimeLimitPropertyDto> updateTimeLimitProperty(UpdateTimeLimitPropertyDto dto);

    @Data(staticConstructor = "of")
    class UpdateTimeLimitPropertyDto {
        private final Long boardId;
        private final TimeLimitBoardProperty timeLimitProperty;
    }


    Exceptionable<Boolean, Long> completelyDeleteBoard(Long boardId);


    Exceptionable<Boolean, Long> softDeleteBoard(Long boardId);

}
