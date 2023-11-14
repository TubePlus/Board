package com.tubeplus.board_service.adapter.rdb.persistence.board;

import com.tubeplus.board_service.adapter.rdb.persistence.board.dao.BoardJpaDataRepository;
import com.tubeplus.board_service.adapter.rdb.persistence.board.dao.BoardQDslRepositoryCustom;
import com.tubeplus.board_service.application.board.domain.Board;
import com.tubeplus.board_service.application.board.port.out.BoardPersistent;
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
    public Exceptionable<Board, SaveDto> saveBoard(SaveDto saveDto) {

        Function<SaveDto, Board> saveBoardByDto =
                dto -> {
                    BoardEntity boardEntity
                            = BoardEntity.builtFrom(dto);

                    BoardEntity savedEntity
                            = jpaDataRepo.save(boardEntity);

                    return savedEntity.buildDomain();
                };

        return new Exceptionable<>(saveBoardByDto, saveDto);
    }


    @Override
    public Exceptionable<Board, Long> findBoard(Long boardId) {

        Function<Long, Board> findBoardById =
                id -> {
                    BoardEntity foundEntity
                            = jpaDataRepo.findById(id)
                            .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_RESOURCE));

                    return foundEntity.buildDomain();
                };

        return new Exceptionable<>(findBoardById, boardId);
    }

    @Override
    public Exceptionable<List<Board>, ListFindDto> findBoardList(ListFindDto listFindDto) {

        Function<ListFindDto, List<Board>> findBoardList =
                dto -> {
                    List<BoardEntity> foundBoardEntities
                            = queryDslRepo.findBoards(dto);

                    List<Board> foundBoards
                            = foundBoardEntities.stream()
                            .map(BoardEntity::buildDomain)
                            .collect(Collectors.toList());

                    return foundBoards;
                };

        return new Exceptionable<>(findBoardList, listFindDto);
    }


    @Override
    public Exceptionable<Boolean, Long> completelyDeleteBoard(Long boardId) {

        Function<Long, Boolean> deleteBoardById =
                id -> {
                    jpaDataRepo.deleteById(boardId);
                    return true;
                };

        return new Exceptionable<>(deleteBoardById, boardId);

    }


    @Override
    public Exceptionable<Boolean, Long> softDeleteBoard(Long boardId) {
        return new Exceptionable<>(queryDslRepo::softDeleteBoard, boardId);
    }


    @Override
    public Exceptionable<Boolean, UpdateCommonPropertyDto> updateBoard(UpdateCommonPropertyDto dto) {
        return Exceptionable.act(queryDslRepo::updateBoard, dto);
    }


}
