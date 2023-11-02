package com.tubeplus.board_service.adapter.rdb.persistence.board;

import com.tubeplus.board_service.adapter.rdb.persistence.board.dao.BoardJpaDataRepository;
import com.tubeplus.board_service.adapter.rdb.persistence.board.dao.BoardQDslRepositoryCustom;
import com.tubeplus.board_service.board.domain.Board;
import com.tubeplus.board_service.board.port.out.BoardPersistent;
import com.tubeplus.board_service.adapter.web.error.BusinessException;
import com.tubeplus.board_service.adapter.web.error.ErrorCode;
import com.tubeplus.board_service.global.Exceptionable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


@Slf4j
@Component
@RequiredArgsConstructor
public class BoardPersistence implements BoardPersistent {

    private final BoardJpaDataRepository jpaDataRepo;
    private final BoardQDslRepositoryCustom queryDslRepo;

    @Override
    public Exceptionable<Board, SaveDto> saveBoard(SaveDto dto) {
        return new Exceptionable<>(this::saveBoardByDto, dto);
    }

    protected Board saveBoardByDto(SaveDto dto) {
        log.info(dto.toString());

        BoardEntity boardEntity
                = BoardEntity.builtOf(dto);
        log.info(boardEntity.toString());

        BoardEntity savedEntity
                = jpaDataRepo.save(boardEntity);
        log.info(savedEntity.toString());

        return savedEntity.buildBoard();
    }


    @Override
    public Exceptionable<Board, Long> findBoard(Long boardId) {
        return new Exceptionable<>(this::findBoardById, boardId);
    }

    protected Board findBoardById(Long boardId) {

        log.info(boardId.toString());

        BoardEntity foundEntity
                = jpaDataRepo.findById(boardId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_RESOURCE));
        log.info(foundEntity.toString());

        return foundEntity.buildBoard();
    }


    @Override
    public Exceptionable<List<Board>, ListFindDto> findBoardList(ListFindDto dto) {
        return new Exceptionable<>(this::findBoardListByDto, dto);
    }

    public List<Board> findBoardListByDto(ListFindDto dto) {
        log.info(dto.toString());

        List<BoardEntity> foundBoardEntities
                = queryDslRepo.findBoards(dto);
        log.info(foundBoardEntities.toString());

        List<Board> foundBoards
                = foundBoardEntities.stream().map(
                BoardEntity::buildBoard
        ).collect(Collectors.toList());
        foundBoards.forEach(
                board -> log.info(board.toString())
        );

        return foundBoards;
    }


    @Override
    public Exceptionable<Boolean, Long> completelyDeleteBoard(Long boardId) {
        return new Exceptionable<>(this::deleteBoardById, boardId);
    }

    public Boolean deleteBoardById(Long boardId) {

        jpaDataRepo.deleteById(boardId);

        return true;
    }


    @Override
    public Exceptionable<Boolean, Long> softDeleteBoard(Long boardId) {
        return new Exceptionable<>(queryDslRepo::softDeleteBoard, boardId);
    }


    @Override
    public Exceptionable<Boolean, UpdateDto> updateBoard(UpdateDto dto) {
        return new Exceptionable<>(queryDslRepo::updateBoard, dto);
    }


}
