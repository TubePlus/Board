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


    private final BusinessException saveFailed = new BusinessException(ErrorCode.SAVE_ENTITY_FAILED);

    @Override
    public Board makeBoard(FormToMakeBoard formToMake) {

        BoardPersistent.SaveDto saveDto
                = BoardPersistent.SaveDto.of(formToMake);

        Board madeBoard = boardPersistence.saveBoard(saveDto)
                .ifExceptioned.thenThrow(saveFailed);

        return madeBoard;
    }


    private final BusinessException findFailed = new BusinessException(ErrorCode.FIND_ENTITY_FAILED);

    @Override
    public List<Board> findCommuBoards(BoardsFindInfo findInfo) {

        BoardPersistent.FindListDto dto = BoardPersistent.FindListDto.of(findInfo);
        //todo communityId = null이면 : 관리자용으로 전부 찾기 기능 만들거면 권한체크, 아니라면 exception
        List<Board> foundBoards = boardPersistence.findBoardList(dto)
                .ifExceptioned.thenThrow(findFailed);

        if (foundBoards.isEmpty()) throw new BusinessException(ErrorCode.NOT_FOUND_RESOURCE);

        return foundBoards;
    }

    @Override
    public Board findBoard(Long boardId) {

        Board foundBoard = boardPersistence.findBoard(boardId)
                .ifExceptioned.thenThrow(findFailed);

        return foundBoard;
    }
}
