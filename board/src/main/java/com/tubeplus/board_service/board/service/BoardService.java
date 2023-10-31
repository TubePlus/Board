package com.tubeplus.board_service.board.service;

import com.tubeplus.board_service.board.model.Board;
import com.tubeplus.board_service.board.port.in.BoardUseCase;
import com.tubeplus.board_service.board.port.out.BoardPersistent;

import com.tubeplus.board_service.adapter.web.error.BusinessException;
import com.tubeplus.board_service.adapter.web.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("Board service")
@RequiredArgsConstructor
@Slf4j
public class BoardService implements BoardUseCase {

    //member variables

    private final BoardPersistent boardPersistence;

    @Override
    public Board makeBoard(MakeBoardForm formToMake) {

        BoardPersistent.SaveDto saveDto
                = BoardPersistent.SaveDto.of(formToMake);

        Board madeBoard = boardPersistence.saveBoard(saveDto)
                .ifExceptioned.throwOf(ErrorCode.SAVE_ENTITY_FAILED);

        return madeBoard;
    }

    @Override
    public List<Board> listCommuBoards(BoardListInfo findInfo) {

        BoardPersistent.ListFindDto listFindDto
                = BoardPersistent.ListFindDto.of(findInfo);

        List<Board> foundBoards
                = boardPersistence.findBoardList(listFindDto)
                .ifExceptioned.throwOf(ErrorCode.FIND_ENTITY_FAILED);

        if (foundBoards.isEmpty())
            throw new BusinessException(ErrorCode.NOT_FOUND_RESOURCE);

        return foundBoards;
    }

    @Override
    public Board findBoard(Long boardId) {

        Board foundBoard
                = boardPersistence.findBoard(boardId)
                .ifExceptioned.throwOf(ErrorCode.FIND_ENTITY_FAILED);

        return foundBoard;
    }

    @Override
    public void updateBoardProperty(Long boardId, BoardProperty property) {

        BoardPersistent.UpdateDto updateDto
                = BoardPersistent.UpdateDto.builtFrom(boardId, property);

        Boolean isUpdated
                = boardPersistence.updateBoard(updateDto)
                .ifExceptioned.throwOf(ErrorCode.UPDATE_ENTITY_FAILED);

        if (!isUpdated)
            throw new BusinessException(ErrorCode.UPDATE_ENTITY_FAILED);
    }

    @Override
    public void completelyDeleteBoard(Long boardId) {

        boardPersistence.completelyDeleteBoard(boardId)
                .ifExceptioned.throwOf(ErrorCode.DELETE_ENTITY_FAILED);
    }

    @Override
    public void softlyDeleteBoard(Long boardId) {

        Boolean softDeleted
                = boardPersistence.softlyDeleteBoard(boardId)
                .ifExceptioned.throwOf(ErrorCode.DELETE_ENTITY_FAILED);

        if (!softDeleted)
            throw new BusinessException(ErrorCode.DELETE_ENTITY_FAILED);
    }

}
