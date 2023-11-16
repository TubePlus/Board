package com.tubeplus.board_service.adapter.rdb.persistence.board;

import com.tubeplus.board_service.adapter.rdb.persistence.board.BoardEntity.BoardEntityBuilder;
import com.tubeplus.board_service.adapter.rdb.persistence.board.dao.BoardJpaDataRepository;
import com.tubeplus.board_service.adapter.rdb.persistence.board.dao.BoardQDslRepositoryCustom;
import com.tubeplus.board_service.application.board.domain.Board;
import com.tubeplus.board_service.application.board.port.in.BoardUseCase.BoardProperty.TimeLimitBoardProperty;
import com.tubeplus.board_service.application.board.port.out.BoardPersistable;
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
@RequiredArgsConstructor
@Component("boardPersistence")
public class BoardPersistence implements BoardPersistable {

    private final BoardJpaDataRepository jpaDataRepo;
    private final BoardQDslRepositoryCustom queryDslRepo;

    @Override
    public Exceptionable<Board, SaveBoardDto> saveBoard(SaveBoardDto saveDto) {

        Function<SaveBoardDto, Board> saveBoardByDto = dto -> {

            BoardEntity boardEntity
                    = BoardEntity.builtFrom(dto);

            BoardEntity savedEntity
                    = jpaDataRepo.save(boardEntity);

            return savedEntity.buildDomain();
        };

        return Exceptionable.act(saveBoardByDto, saveDto);
    }


    @Override
    public Exceptionable<Board, Long> findBoard(Long boardId) {

        Function<Long, Board> findBoardById = id -> {

            BoardEntity foundEntity
                    = jpaDataRepo.findById(id)
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_RESOURCE));

            return foundEntity.buildDomain();
        };

        return new Exceptionable<>(findBoardById, boardId);
    }

    @Override
    public Exceptionable<List<Board>, FindBoardListDto> findBoardList(FindBoardListDto findDto) {

        Function<FindBoardListDto, List<Board>> findBoardList = dto -> {
            List<BoardEntity> foundBoardEntities = queryDslRepo.findBoards(dto);

            List<Board> foundBoards = foundBoardEntities.stream().map(BoardEntity::buildDomain).collect(Collectors.toList());

            return foundBoards;
        };

        return new Exceptionable<>(findBoardList, findDto);
    }


    @Override
    public Exceptionable<Boolean, Long> completelyDeleteBoard(Long boardId) {

        Function<Long, Boolean> deleteBoardById = id -> {
            jpaDataRepo.deleteById(boardId);
            return true;
        };

        return Exceptionable.act(deleteBoardById, boardId);
    }


    @Override
    public Exceptionable<Boolean, Long> softDeleteBoard(Long boardId) {
        return Exceptionable.act(queryDslRepo::softDeleteBoard, boardId);
    }


    @Override
    public Exceptionable<Boolean, UpdateCommonPropertyDto> updateCommonProperty(UpdateCommonPropertyDto updateCommonPropertyDto) {
        return Exceptionable.act(queryDslRepo::updateCommonProperty, updateCommonPropertyDto);
    }

    @Override
    public Exceptionable<Boolean, UpdateTimeLimitPropertyDto> updateTimeLimitProperty(UpdateTimeLimitPropertyDto updateTimeLimitDto) {

        return Exceptionable.act(dto -> {

            BoardEntity updateTarget
                    = jpaDataRepo.findById(dto.getBoardId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.FIND_ENTITY_FAILED));

            TimeLimitBoardProperty timeLimitProperty
                    = dto.getTimeLimitProperty();

            updateTarget.setLimitDateTime(timeLimitProperty.getLimitDateTime());

            BoardEntity updatedEntity
                    = jpaDataRepo.save(updateTarget);

            return updatedEntity.equals(updateTarget);

        }, updateTimeLimitDto);

    }


}
