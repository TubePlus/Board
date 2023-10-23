package com.tubeplus.board_service.external.rdb.driven_adapter.dao;

import com.ctc.wstx.util.StringUtil;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tubeplus.board_service.domain.board.port.out.BoardPersistent;
import com.tubeplus.board_service.external.rdb.entity.BoardEntity;
import com.tubeplus.board_service.external.rdb.entity.QBoardEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class BoardQDslRepositoryImpl implements BoardQDslRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public List<BoardEntity> findBoards(BoardPersistent.FindDto dto) {

        QBoardEntity board = QBoardEntity.boardEntity;


        BooleanExpression commuIdEq
                = board.communityId.eq(dto.getCommunityId());

        BooleanBuilder accessStatus = new BooleanBuilder();
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
                .where(commuIdEq.and(accessStatus).and(nameLike))
                .fetch();
    }

}