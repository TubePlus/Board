package com.tubeplus.board_service.application.board.service;

import com.tubeplus.board_service.application.board.domain.Board;
import com.tubeplus.board_service.application.board.port.in.BoardUseCase;
import com.tubeplus.board_service.application.board.port.out.BoardPersistable;

import com.tubeplus.board_service.adapter.web.error.BusinessException;
import com.tubeplus.board_service.adapter.web.error.ErrorCode;
import com.tubeplus.board_service.application.board.port.out.BoardPersistable.FindBoardListDto;
import com.tubeplus.board_service.application.board.port.out.BoardPersistable.SaveBoardDto;
import com.tubeplus.board_service.application.board.port.out.BoardPersistable.UpdateCommonPropertyDto;
import com.tubeplus.board_service.application.board.port.out.BoardPersistable.UpdateTimeLimitPropertyDto;
import com.tubeplus.board_service.application.board.port.out.BoardEventPublishable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@EnableScheduling

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService implements BoardUseCase {

    private final BoardPersistable boardPersistence;

    private final BoardEventPublishable eventPublisher;


    //todo limit time 지나면 삭제하는 스케줄러 달기
//    @Scheduled(cron = "0 0 3 0/7 * *")
//    private void deleteBoardsAfterTimeLimit() {
//
//        List<Board> timeLimitBoards
//                = boardPersistence.findTimeLimitBoards()
//                .ifExceptioned.thenThrow(ErrorCode.FIND_ENTITY_FAILED)
//                .stream()
//                .filter(this::isNowValidBoard)
//                .collect(Collectors.toList());
//
//        if (timeLimitBoards.isEmpty()) return;
//
//        timeLimitBoards.forEach(board -> {
//
//            boardPersistence.softDeleteBoard(board.getId())
//                    .ifExceptioned.thenThrow(ErrorCode.DELETE_ENTITY_FAILED);
//
//            eventPublisher.publishBoardDeleted(board);
//        });
//
//        lastDeletedTimeLimitBoardId = timeLimitBoards.get(timeLimitBoards.size() - 1).getId();
//    }


    @Override
    public Board createBoard(CreateBoardForm createForm) {

        SaveBoardDto saveDto
                = SaveBoardDto.of(createForm);

        Board createdBoard
                = boardPersistence.saveBoard(saveDto)
                .ifExceptioned.thenThrow(ErrorCode.SAVE_ENTITY_FAILED);

        eventPublisher.publishBoardCreated(createdBoard);

        return createdBoard;
    }


    @Override
    public List<Board> findCommuBoards(FindBoardsInfo findInfo) {

        FindBoardListDto findDto
                = FindBoardListDto.of(findInfo);

        List<Board> foundBoards
                = boardPersistence.findBoardList(findDto)
                .ifExceptioned.thenThrow(ErrorCode.FIND_ENTITY_FAILED)
                .stream()
                .filter(this::isNowValidBoard)
                .collect(Collectors.toList());


        if (foundBoards.isEmpty())
            throw new BusinessException(ErrorCode.NOT_FOUND_RESOURCE);

        return foundBoards;
    }

    private boolean isNowValidBoard(Board board) {

        LocalDateTime boardLimitTime
                = board.getLimitDateTime();

        if (boardLimitTime == null) return true;

        return LocalDateTime.now().isBefore(boardLimitTime);
    }


    @Override
    public Board findBoard(Long boardId) {

        Board foundBoard
                = boardPersistence.findBoard(boardId)
                .ifExceptioned.thenThrow(ErrorCode.FIND_ENTITY_FAILED);

        return foundBoard;
    }


    @Override
    public void updateBoardProperty(Long boardId, BoardProperty updateInfo) {

        Boolean updated = true;

        if (updateInfo.getCommonProperty() != null) {

            UpdateCommonPropertyDto dto
                    = UpdateCommonPropertyDto.builtFrom(boardId, updateInfo.getCommonProperty());

            Boolean commonPropertyUpdated
                    = boardPersistence.updateCommonProperty(dto)
                    .ifExceptioned.thenThrow(ErrorCode.UPDATE_ENTITY_FAILED);

            updated &= commonPropertyUpdated;
        }

        if (updateInfo.getTimeLimitProperty() != null) {

            UpdateTimeLimitPropertyDto dto
                    = UpdateTimeLimitPropertyDto.of(boardId, updateInfo.getTimeLimitProperty());

            Boolean timeLimitPropertyUpdated
                    = boardPersistence.updateTimeLimitProperty(dto)
                    .ifExceptioned.thenThrow(ErrorCode.UPDATE_ENTITY_FAILED);

            updated &= timeLimitPropertyUpdated;
        }

        if (!updated)
            throw new BusinessException(ErrorCode.UPDATE_ENTITY_FAILED);
    }

    @Override
    public void completelyDeleteBoard(Long boardId) {

        boardPersistence.completelyDeleteBoard(boardId)
                .ifExceptioned.thenThrow(ErrorCode.DELETE_ENTITY_FAILED);
    }

    @Override
    public void softlyDeleteBoard(Long boardId) {

        Boolean softDeleted
                = boardPersistence.softDeleteBoard(boardId)
                .ifExceptioned.thenThrow(ErrorCode.DELETE_ENTITY_FAILED);

        if (!softDeleted)
            throw new BusinessException(ErrorCode.DELETE_ENTITY_FAILED);
    }

}
