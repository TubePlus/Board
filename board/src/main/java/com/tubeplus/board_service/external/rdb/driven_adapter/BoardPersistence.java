package com.tubeplus.board_service.external.rdb.driven_adapter;

import com.tubeplus.board_service.external.rdb.driven_adapter.dao.BoardJpaDataRepository;
import com.tubeplus.board_service.external.rdb.driven_adapter.dao.BoardQDslRepositoryCustom;
import com.tubeplus.board_service.external.rdb.entity.BoardEntity;
import com.tubeplus.board_service.domain.board.model.Board;
import com.tubeplus.board_service.domain.board.port.out.BoardPersistent;
import com.tubeplus.board_service.global.Exceptionable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;


@Slf4j
@Component
@RequiredArgsConstructor
public class BoardPersistence implements BoardPersistent {

    private final BoardJpaDataRepository jpaDataRepo;
    private final BoardQDslRepositoryCustom queryDslRepo;


    private final InternalLogic internalLogic
            = new InternalLogic();
    public final Function<SaveDto, Board> logicSave
            = this.internalLogic::saveBoard;
    public final Function<FindListDto, List<Board>> logicFindList
            = this.internalLogic::findBoardList;
    private Function<Long, Board> logicFind
            = this.internalLogic::findBoard;

//todo 접근제어자 private class InternalLogic.public Board functions로 바꿔서 시도
//todo internal logic 클래스 분리해 default 생성자만 갖고 함수는 public으로 하던지 해서 깔끔하게 정리
    public class InternalLogic {

        public Board saveBoard(SaveDto dto) {
            log.info(dto.toString());

            BoardEntity boardEntity = BoardEntity.builtOf(dto);
            log.info(boardEntity.toString());

            BoardEntity savedEntity = jpaDataRepo.save(boardEntity);
            log.info(savedEntity.toString());

            return savedEntity.buildBoard();
        }

        public List<Board> findBoardList(FindListDto dto) {
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

        public Board findBoard(Long boardId) {
            log.info(boardId.toString());

            BoardEntity foundEntity = jpaDataRepo.findById(boardId)
                    .orElseThrow();
            log.info(foundEntity.toString());

            return foundEntity.buildBoard();
        }
    }



    //todo 메서드 내부 변수로 갖게 해서 쓸데없는 동적할당 피하기
    @Override
    public Exceptionable<Board, SaveDto> saveBoard(SaveDto dto) {
        return new Exceptionable<>(this.logicSave, dto);
    }

    @Override
    public Exceptionable<List<Board>, FindListDto> findBoardList(FindListDto dto) {
        return new Exceptionable<>(this.logicFindList, dto);
    }

    @Override
    public Exceptionable<Board, Long> findBoard(Long boardId) {
        return new Exceptionable<>(this.logicFind, boardId);
    }

}
