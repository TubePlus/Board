package com.tubeplus.board_service.adapter.rdb.persistence.board.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.tubeplus.board_service.adapter.rdb.persistence.board.QBoardEntity;
import com.tubeplus.board_service.board.port.out.BoardPersistent;
import com.tubeplus.board_service.adapter.rdb.persistence.board.BoardEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;


@Slf4j
@Repository
@RequiredArgsConstructor
public class BoardQDslRepositoryImpl implements BoardQDslRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public List<BoardEntity> findBoards(BoardPersistent.ListFindDto dto) {

        QBoardEntity board
                = QBoardEntity.boardEntity;


        BooleanExpression commuId
                = board.communityId.eq(dto.getCommunityId());

        BooleanBuilder accessStatus
                = new BooleanBuilder();
        if (dto.getVisible() != null)
            accessStatus.and(board.visible.eq(dto.getVisible()));
        if (dto.getErase() != null)
            accessStatus.and(board.erase.eq(dto.getErase()));


        BooleanExpression nameLike
                = StringUtils.hasText(dto.getNameToSearch())
                ? board.boardName.like("%" + dto.getNameToSearch() + "%")
                : null;


        return queryFactory
                .selectFrom(board)
                .where(commuId.and(accessStatus).and(nameLike))
                .fetch();
    }

    @Override
    @Transactional //todo 엔티티메니저.flush,clear로 수정후 테스트, 좀 더 알아보고 쓸데없이 작업 두번세번 안하게 리팩토링
    public Boolean updateBoard(BoardPersistent.UpdateDto dto) {

        QBoardEntity board
                = QBoardEntity.boardEntity;

        JPAUpdateClause updateQuery
                = queryFactory.update(board)
                .where(board.id.eq(dto.getId()));

        writeUpdatesToQuery(updateQuery, dto, board);


        return updateQuery.execute() != 0;
    }

    private void writeUpdatesToQuery(JPAUpdateClause updateQuery,
                                     BoardPersistent.UpdateDto dto,
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
        if (dto.getLimitDateTime() != null) {
            updateQuery.set(board.limitDateTime, dto.getLimitDateTime());
        }
        if (dto.getErase() != null) {
            updateQuery.set(board.erase, dto.getErase());
        }

    }


    @Transactional
    @Override
    public boolean softDeleteBoard(Long boardId) {

        QBoardEntity board
                = QBoardEntity.boardEntity;

        long executeResult =
                queryFactory.update(board)
                        .where(board.id.eq(boardId))
                        .set(board.erase, true)
                        .execute();

        return executeResult != 0;
    }

}

//write query by reflection

/*        Class<EntityPathBase> qEntityClazz = (Class<EntityPathBase>) board.getClass();

        long updateColCount = 0;
        Class<Path> qPathInterface = Path.class;


        for (Field qField : qEntityClazz.getFields()) {

            Class<?> qFieldType = qField.getType();
            if (qPathInterface.isAssignableFrom(qFieldType)) {

                Path<?> pathField;
                try {
                    pathField = (Path<?>) qField.get(board);
                } catch (IllegalAccessException e) {//todo throw시 e.getCause도 첨부해서 throw
                    throw new RuntimeException(String.format("writeUpdatesToQuery by dto(%s) pathField assigned  threw IllegalAccessException\n", dto.toString()));
                }

                String dtoFieldName = pathField.getMetadata().getName();
                try {
                    Field dtoUpdateField = dto.getClass().getField(dtoFieldName);

                    updateQuery.set(pathField, dtoUpdateField.getType().cast(dtoUpdateField.get(board)));

                } catch (Exception e) {
                    throw new RuntimeException(String.format("writeUpdatesToQuery by dto(%s)  threw \n", dto.toString()));
                }

                updateColCount += 1;

            }
        }

        if (dto.getBoardName() != null)
            updateQuery.set(board.boardName, dto.getBoardName()); */

