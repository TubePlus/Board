package com.tubeplus.board_service.adapter.rdb.board.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.tubeplus.board_service.adapter.rdb.board.BoardEntity;
import com.tubeplus.board_service.adapter.rdb.board.QBoardEntity;
import com.tubeplus.board_service.application.board.port.out.BoardPersistable.FindBoardListDto;
import com.tubeplus.board_service.application.board.port.out.BoardPersistable.UpdateCommonPropertyDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

import static java.lang.String.format;


@Slf4j
@Repository
@RequiredArgsConstructor
public class BoardQDslRepositoryImpl implements BoardQDslRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public List<BoardEntity> findBoards(FindBoardListDto dto) {

        QBoardEntity board
                = QBoardEntity.boardEntity;

        BooleanExpression commuIdEq
                = board.communityId.eq(dto.getCommunityId());

        BooleanBuilder accessStatusEq
                = new BooleanBuilder();
        if (dto.getVisible() != null)
            accessStatusEq.and(board.visible.eq(dto.getVisible()));
        if (dto.getSoftDelete() != null)
            accessStatusEq.and(board.softDelete.eq(dto.getSoftDelete()));


        BooleanExpression boardNameLike
                = StringUtils.hasText(dto.getNameToSearch())
                ? board.boardName.like("%" + dto.getNameToSearch() + "%")
                : null;


        return queryFactory
                .selectFrom(board)
                .where(commuIdEq.and(accessStatusEq).and(boardNameLike))
                .fetch();
    }

    @Override
    //todo 엔티티메니저.flush,clear로 수정후 테스트, 좀 더 알아보고 쓸데없이 작업 두번세번 안하게 리팩토링
    @Transactional
    public Boolean updateCommonProperty(UpdateCommonPropertyDto dto) {

        QBoardEntity board
                = QBoardEntity.boardEntity;

        JPAUpdateClause updateQuery
                = queryFactory.update(board)
                .where(board.id.eq(dto.getId()));

        writeUpdatesToQuery(updateQuery, dto, board);

        long execute = updateQuery.execute();
        log.info("execute" + execute);
        return execute != 0;
    }

    private void writeUpdatesToQuery(JPAUpdateClause updateQuery,
                                     UpdateCommonPropertyDto dto,
                                     QBoardEntity board) {

        if (dto.getBoardName() != null) {
            updateQuery.set(board.boardName, dto.getBoardName());
        }
        if (dto.getBoardType() != null) {
            updateQuery.set(board.boardType, dto.getBoardType());
        }
        if (dto.getBoardDescription() != null) {
            updateQuery.set(board.boardDescription, dto.getBoardDescription());
        }
        if (dto.getVisible() != null) {
            updateQuery.set(board.visible, dto.getVisible());
        }
        if (dto.getSoftDelete() != null) {
            updateQuery.set(board.softDelete, dto.getSoftDelete());
        }

    }


    @Transactional
    @Override
    public boolean softDeleteBoard(Long boardId) {

        QBoardEntity board
                = QBoardEntity.boardEntity;

        long executeResult
                = queryFactory.update(board)
                .where(board.id.eq(boardId))
                .set(board.softDelete, true)
                .execute();

        return executeResult != 0;
    }
}

//    public void updateClauseSetter(JPAUpdateClause updateQuery,
//                                   BoardPersistable.UpdateCommonPropertyDto dto,
//                                   QBoardEntity board) {
//
//        Class<BoardEntity> qEntityClazz = BoardEntity.class;
//        Class<Path> qPathInterface = Path.class;
//
//        for (Field qField : qEntityClazz.getFields()) {
//
//            if (qPathInterface.isAssignableFrom(qField.getType())) {
//
//                Path<?> pathField;
//                try {
//                    pathField = (Path<?>) qField.get(board);
//                } catch (IllegalAccessException e) {
//                    throw new RuntimeException(
//                            format("writeUpdatesToQuery by dto(%s) pathField assigned  threw IllegalAccessException\n", dto.toString())
//                            , e.getCause());
//                }
//
//                String dtoFieldName = pathField.getMetadata().getName();
//                try {
//                    Field dtoUpdateField = dto.getClass().getField(dtoFieldName);
//
//                    updateQuery.set(pathField, dtoUpdateField.getType().cast(dtoUpdateField.get(board)));
//
//                } catch (Exception e) {
//                    throw new RuntimeException(format("writeUpdatesToQuery by dto(%s)  threw \n", dto.toString()));
//                }
//
//                updateColCount += 1;
//
//            }
//        }
//
//        if (dto.getBoardName() != null)
//            updateQuery.set(board.boardName, dto.getBoardName());
//    }

