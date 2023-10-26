package com.tubeplus.board_service.domain.board.service;

import com.tubeplus.board_service.domain.board.model.Board;
import com.tubeplus.board_service.domain.board.port.in.BoardUseCase;
import com.tubeplus.board_service.domain.board.port.out.BoardPersistent;

import com.tubeplus.board_service.external.web.error.BusinessException;
import com.tubeplus.board_service.external.web.error.ErrorCode;
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
    public Board makeBoard(FormToMakeBoard formToMake) {

        BoardPersistent.SaveDto saveDto
                = BoardPersistent.SaveDto.of(formToMake);

        Board madeBoard = boardPersistence.saveBoard(saveDto)
                .ifExceptioned.thenThrowOf(ErrorCode.SAVE_ENTITY_FAILED);

        return madeBoard;
    }


    @Override
    public List<Board> listCommuBoards(BoardListInfo findInfo) {

        BoardPersistent.ListFindDto listFindDto
                = BoardPersistent.ListFindDto.of(findInfo);

        List<Board> foundBoards
                = boardPersistence.findBoardList(listFindDto)
                .ifExceptioned.thenThrowOf(ErrorCode.FIND_ENTITY_FAILED);

        if (foundBoards.isEmpty())
            throw new BusinessException(ErrorCode.NOT_FOUND_RESOURCE);

        return foundBoards;
    }

    @Override
    public Board findBoard(Long boardId) {

        Board foundBoard
                = boardPersistence.findBoard(boardId)
                .ifExceptioned.thenThrowOf(ErrorCode.FIND_ENTITY_FAILED);

        return foundBoard;
    }


    @Override
    public void updateBoardProperty(Long boardId, BoardProperty property) {

        BoardPersistent.UpdateDto updateDto
                = BoardPersistent.UpdateDto.builtFrom(boardId, property);

        Boolean isUpdated
                = boardPersistence.updateBoard(updateDto)
                .ifExceptioned
                .thenThrowOf(ErrorCode.UPDATE_ENTITY_FAILED);

        if (!isUpdated)
            throw new BusinessException(ErrorCode.UPDATE_ENTITY_FAILED);
    }


}
