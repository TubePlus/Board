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
import java.util.stream.Collectors;


@Slf4j
@Component
@RequiredArgsConstructor
public class BoardPersistence implements BoardPersistent {

    private final BoardJpaDataRepository jpaDataRepo;
    private final BoardQDslRepositoryCustom queryDslRepo;


    public final CoreLogic coreLogic = new CoreLogic();
//todo corelogic의 함수 포인터 static final변수로 선언해 불필요한 동적할당 줄이기
    public class CoreLogic {

        public Board saveBoard(SaveDto dto) {
            log.info(dto.toString());
            BoardEntity boardEntity = BoardEntity.builtOf(dto);

            BoardEntity savedEntity = jpaDataRepo.save(boardEntity);

            return savedEntity.buildBoard();
        }

        public List<Board> findBoards(FindDto findDto) {
            List<BoardEntity> foundBoardEntities = queryDslRepo.findBoards(findDto);

            List<Board> foundBoards
                    = foundBoardEntities.stream().map
                            (BoardEntity::buildBoard)
                    .collect(Collectors.toList());

            return foundBoards;
        }
    }


    @Override
    public Exceptionable<Board, SaveDto> saveBoard(SaveDto dto) {

        return new Exceptionable<>(this.coreLogic::saveBoard, dto);
    }

    @Override
    public Exceptionable<List<Board>, FindDto> findBoards(FindDto findDto) {
        return new Exceptionable<>(this.coreLogic::findBoards, findDto);
    }

}
