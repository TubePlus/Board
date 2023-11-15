package com.tubeplus.board_service.application.board.service;

import com.tubeplus.board_service.application.board.domain.Board;
import com.tubeplus.board_service.application.board.port.in.BoardUseCase;
import com.tubeplus.board_service.application.board.port.out.BoardPersistable;

import com.tubeplus.board_service.adapter.web.error.BusinessException;
import com.tubeplus.board_service.adapter.web.error.ErrorCode;
import com.tubeplus.board_service.application.board.port.out.BoardPersistable.SaveBoardDto;
import com.tubeplus.board_service.application.board.port.out.BoardPersistable.UpdateCommonPropertyDto;
import com.tubeplus.board_service.application.board.port.out.BoardPersistable.UpdateTimeLimitPropertyDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("Board service")
@RequiredArgsConstructor
@Slf4j
public class BoardService implements BoardUseCase {

    //member variables

    private final BoardPersistable boardPersistence;

    @Override
    public Board makeBoard(MakeBoardForm formToMake) {

        SaveBoardDto saveDto
                = SaveBoardDto.of(formToMake);

        Board madeBoard
                = boardPersistence.saveBoard(saveDto)
                .ifExceptioned.thenThrow(ErrorCode.SAVE_ENTITY_FAILED);

        return madeBoard;
    }

    @Override
    public List<Board> listCommuBoards(BoardListInfo findInfo) {

        BoardPersistable.FindBoardListDto findDto
                = BoardPersistable.FindBoardListDto.of(findInfo);

        List<Board> foundBoards
                = boardPersistence.findBoardList(findDto)
                .ifExceptioned.thenThrow(ErrorCode.FIND_ENTITY_FAILED);

        if (foundBoards.isEmpty())
            throw new BusinessException(ErrorCode.NOT_FOUND_RESOURCE);

        return foundBoards;
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
