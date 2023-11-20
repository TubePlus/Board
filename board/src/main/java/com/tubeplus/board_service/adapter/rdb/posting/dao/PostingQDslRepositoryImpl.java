package com.tubeplus.board_service.adapter.rdb.posting.dao;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tubeplus.board_service.adapter.rdb.posting.entity.PostingEntity;
import com.tubeplus.board_service.adapter.rdb.posting.entity.QPostingEntity;
import com.tubeplus.board_service.application.posting.port.out.PostingPersistable.FindPostingsDto.SortedFindRange;
import com.tubeplus.board_service.global.Exceptionable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.tubeplus.board_service.application.posting.port.out.PostingPersistable.*;


@Slf4j
@Repository
@RequiredArgsConstructor
public class PostingQDslRepositoryImpl implements PostingQDslRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    @Transactional(readOnly = true)
    public Long countPostingEntities(FindPostingsDto.FieldsFindCondition condition) {

        QPostingEntity posting = QPostingEntity.postingEntity;

        // query 생성
        JPAQuery<Long> query
                = queryFactory.select(posting.count())
                .from(posting)
                .where(equalsToConditionByFields(posting, condition));

        // count query 실행 및 결과 반환
        Long entitiesNum
                = Exceptionable.act(query::fetchOne)
                .ifExceptioned.thenThrow(new RuntimeException("count postings query failed"));

        return entitiesNum;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existNextPosting(FindPostingsDto dto) {

        QPostingEntity posting = new QPostingEntity("posting");


        JPAQuery<Long> queryToCheckNext
                = queryFactory.select(posting.id)
                .from(posting)
                .where(equalsToConditionByFields(
                        posting, dto.getFieldsFindCondition()));

        writeSortScopeToQuery(dto, posting, queryToCheckNext);


        Long nextPostingId
                = Exceptionable.act(queryToCheckNext::fetchFirst)
                .ifExceptioned.thenThrow(new RuntimeException("exist next posting query failed"));

        return nextPostingId != null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostingEntity> findPostingEntities(FindPostingsDto dto) {


        QPostingEntity posting = QPostingEntity.postingEntity;

        FindPostingsDto.FieldsFindCondition condition = dto.getFieldsFindCondition();

        // query 생성
        JPAQuery<PostingEntity> query
                = queryFactory.selectFrom(posting)
                .where(equalsToConditionByFields(posting, condition));

        writeSortScopeToQuery(dto, posting, query);


        // query 실행 및 결과 반환
        List<PostingEntity> foundEntities = query.fetch();

        return foundEntities;
    }

    private void writeSortScopeToQuery(FindPostingsDto dto, QPostingEntity posting, JPAQuery query) {
        SortedFindRange scope = dto.getSortedRange();
        if (scope.getOffset() != null)
            query.offset(scope.getOffset());
        if (scope.getLimit() != null)
            query.limit(scope.getLimit());
        if (scope.getSortBy() != null) {//todo SortBy 사실은 List로 여러조건 한번에 가질 수 있어야함, 수정
            SortedFindRange.SortBy sortBy = scope.getSortBy();

            OrderSpecifier orderSpecifier
                    = mapToOrderSpec(posting, sortBy);
            query.orderBy(orderSpecifier);
        }
    }

    private OrderSpecifier mapToOrderSpec(QPostingEntity posting, SortedFindRange.SortBy sortBy) {

        ComparableExpressionBase qField;

        switch (sortBy.getPivotField()) {

            case ID -> qField = posting.id;

            case VOTE_COUNT -> qField = posting.voteCount;

            default -> throw new RuntimeException("unexpected sort pivot field");
        }

        OrderSpecifier orderSpecifier
                = sortBy.isAscending()
                ? qField.asc() : qField.desc();

        return orderSpecifier;
    }


    // BooleanExpression 생성 메소드들
    private BooleanExpression equalsToConditionByFields(QPostingEntity posting,
                                                        FindPostingsDto.FieldsFindCondition condition) {

        BooleanExpression findConditionEqual
                = Expressions.TRUE // cursorId가 null일때(= boardId로 검색X) NullPointException 방지
                .and(idLessThanCursor(posting, condition))
                .and(boardIdEq(posting, condition))
                .and(authorUuidEq(posting, condition))
                .and(pinEq(posting, condition))
                .and(titleContains(posting, condition))
                .and(contentContains(posting, condition))
                .and(softDeleteEq(posting, condition));

        return findConditionEqual;
    }

    private BooleanExpression idLessThanCursor(QPostingEntity posting,
                                               FindPostingsDto.FieldsFindCondition condition) {

        if (condition.getCursorId() == null) return null;

        return posting.id.lt(condition.getCursorId());
    }


    private BooleanExpression contentContains(QPostingEntity posting,
                                              FindPostingsDto.FieldsFindCondition condition) {

        if (condition.getContentsContaining() == null) return null;

        return posting.contents.contains(condition.getContentsContaining());
    }

    private BooleanExpression titleContains(QPostingEntity posting,
                                            FindPostingsDto.FieldsFindCondition condition) {

        if (condition.getTitleContaining() == null) return null;

        return posting.title.contains(condition.getTitleContaining());
    }

    private BooleanExpression pinEq(QPostingEntity posting,
                                    FindPostingsDto.FieldsFindCondition condition) {

        if (condition.getPin() == null) return null;

        return posting.pin.eq(condition.getPin());
    }

    private BooleanExpression boardIdEq(QPostingEntity posting,
                                        FindPostingsDto.FieldsFindCondition condition) {

        if (condition.getBoardId() == null) return null;

        return posting.board.id.eq(condition.getBoardId());
    }

    private BooleanExpression authorUuidEq(QPostingEntity posting,
                                           FindPostingsDto.FieldsFindCondition condition) {

        if (condition.getAuthorUuid() == null) return null;

        return posting.authorUuid.eq(condition.getAuthorUuid());
    }

    private BooleanExpression softDeleteEq(QPostingEntity posting,
                                           FindPostingsDto.FieldsFindCondition condition) {

        if (condition.getSoftDelete() == null) return null;

        return posting.softDelete.eq(condition.getSoftDelete());
    }


}


//    @Transactional
//    public long updateSoftDelete(Long postingId) {
//
//        QPostingEntity posting
//                = QPostingEntity.postingEntity;
//
//        long updatedColumns =
//                queryFactory.update(posting)
//                        .where(posting.id.eq(postingId))
//                        .set(posting.softDelete, true)
//                        .execute();
//
//        return updatedColumns;
//    }
//
//    @Transactional
//    public long updatePinReversed(Long postingId) {
//
//        QPostingEntity posting
//                = QPostingEntity.postingEntity;
//
//        long updatedColumns =
//                queryFactory.update(posting)
//                        .where(posting.id.eq(postingId))
//                        .set(posting.pin, posting.pin.not())
//                        .execute();
//
//        return updatedColumns;
//    }

//    public long updatePostingWriting(UpdateArticleDto dto) {
//
//        QPostingEntity posting
//                = QPostingEntity.postingEntity;
//
//        JPAUpdateClause updateClause
//                = queryFactory.update(posting)
//                .where(posting.id.eq(dto.getPostingId()));
//
//        if (dto.getTitle() != null)
//            updateClause.set(posting.title, dto.getTitle());
//        if (dto.getContents() != null)
//            updateClause.set(posting.contents, dto.getContents());
//
//        long updatedColumns
//                = updateClause.execute();
//
//        return updatedColumns;
//    }
