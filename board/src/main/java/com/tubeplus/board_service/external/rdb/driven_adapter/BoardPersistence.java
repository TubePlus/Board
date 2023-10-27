package com.tubeplus.board_service.external.rdb.driven_adapter;

import com.tubeplus.board_service.external.rdb.driven_adapter.dao.BoardJpaDataRepository;
import com.tubeplus.board_service.external.rdb.driven_adapter.dao.BoardQDslRepositoryCustom;
import com.tubeplus.board_service.external.rdb.entity.BoardEntity;
import com.tubeplus.board_service.board.model.Board;
import com.tubeplus.board_service.board.port.out.BoardPersistent;
import com.tubeplus.board_service.external.web.error.BusinessException;
import com.tubeplus.board_service.external.web.error.ErrorCode;
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
        return new Exceptionable<>(this.save, dto);
    }

    @Override
    public Exceptionable<Board, Long> findBoard(Long boardId) {
        return new Exceptionable<>(this.findBoard, boardId);
    }

    @Override
    public Exceptionable<List<Board>, ListFindDto> findBoardList(ListFindDto dto) {
        return new Exceptionable<>(this.findBoardList, dto);
    }

    @Override
    public Exceptionable<Boolean, Long> completelyDeleteBoard(Long boardId) {
        return new Exceptionable<>(this.deleteBoard, boardId);
    }

    @Override
    public Exceptionable<Boolean, UpdateDto> updateBoard(UpdateDto dto) {
        return new Exceptionable<>(this.updateBoard, dto);
    }

    @Override
    public Exceptionable<Boolean, Long> softlyDeleteBoard(Long boardId) {
        return new Exceptionable<>(this.logic::softlyDeleteBoard, boardId);
    }

    private final InternalLogic logic = new InternalLogic();

    public final Function<SaveDto, Board> save = this.logic::saveBoard;
    public final Function<Long, Board> findBoard = this.logic::findBoard;
    public final Function<ListFindDto, List<Board>> findBoardList = this.logic::findBoardList;
    public final Function<UpdateDto, Boolean> updateBoard = this.logic::updateBoard;
    private final Function<Long, Boolean> deleteBoard = this.logic::deleteBoard;


    //todo 접근제어자 private class InternalLogic.public Board functions로 바꿔서 시도
    public class InternalLogic {

        public Board saveBoard(SaveDto dto) {
            log.info(dto.toString());

            BoardEntity boardEntity = BoardEntity.builtOf(dto);
            log.info(boardEntity.toString());

            BoardEntity savedEntity = jpaDataRepo.save(boardEntity);
            log.info(savedEntity.toString());

            return savedEntity.buildBoard();
        }

        public Board findBoard(Long boardId) {

            log.info(boardId.toString());

            BoardEntity foundEntity = jpaDataRepo.findById(boardId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_RESOURCE));
            log.info(foundEntity.toString());

            return foundEntity.buildBoard();
        }

        public List<Board> findBoardList(ListFindDto dto) {
            log.info(dto.toString());

            List<BoardEntity> foundBoardEntities = queryDslRepo.findBoards(dto);
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

        public Boolean updateBoard(UpdateDto dto) {
            return queryDslRepo.updateBoard(dto);
        }

        public Boolean deleteBoard(Long boardId) {

            jpaDataRepo.deleteById(boardId);

            return true;
        }

        public Boolean softlyDeleteBoard(Long boardId) {

            return queryDslRepo.softDeleteBoard(boardId);
        }
    }
}
