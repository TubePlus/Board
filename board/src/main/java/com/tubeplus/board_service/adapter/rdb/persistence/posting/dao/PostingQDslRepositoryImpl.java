package com.tubeplus.board_service.adapter.rdb.persistence.posting.dao;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tubeplus.board_service.adapter.rdb.persistence.posting.PostingEntity;
import com.tubeplus.board_service.adapter.rdb.persistence.posting.QPostingEntity;
import com.tubeplus.board_service.global.Exceptionable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.tubeplus.board_service.application.posting.port.out.PostingPersistent.*;


@Slf4j
@Repository
@RequiredArgsConstructor
public class PostingQDslRepositoryImpl implements PostingQDslRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PostingEntity> pagePostingEntities(PagePostingsDto dto) {

        return this.pagePostingEntities(dto.getFindCondition(), dto.getPageReq(), null);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostingEntity> pagePostingEntities(FindPostingsCondition findCondition,
                                                   PageRequest pageReq,
                                                   OrderSpecifier orderSpec) {

        List<PostingEntity> foundEntities
                = findPostingEntities
                (findCondition, SortScope.of(pageReq, orderSpec), null);


        Page<PostingEntity> pagedEntities
                = PageableExecutionUtils.getPage // count 쿼리 최적화 위해 사용
                (foundEntities, pageReq, () -> countPostingEntities(findCondition));

        return pagedEntities;
    }


    @Override
    @Transactional(readOnly = true)
    public Long countPostingEntities(FindPostingsCondition findCondition) {

        //query에 쓸 변수 선언
        QPostingEntity posting = QPostingEntity.postingEntity;

        BooleanExpression findConditionEqual
                = equalsToFindCondition(posting, findCondition);

        // query 생성
        JPAQuery<Long> query
                = queryFactory.select(posting.count())
                .where(findConditionEqual);


        // query 실행 및 결과 반환
        Long numEntities
                = (Long) Exceptionable.act(query::fetchOne)
                .ifExceptioned.thenThrow(new RuntimeException("게시글 개수 조회 실패"));

        return numEntities;
    }


    @Override
    @Transactional(readOnly = true)
    public List<PostingEntity> findPostingEntities(FindPostingsCondition findCondition,
                                                   SortScope scope,
                                                   Long cursorId) {

        QPostingEntity posting = QPostingEntity.postingEntity;


        // where 구문에 쓸 BooleanExpression 변수들 생성
        // find 조건설정
        BooleanExpression findConditionEqual
                = equalsToFindCondition(posting, findCondition);
        // cursor 조건설정
        BooleanExpression idLessThanCursor
                = cursorId != null
                ? posting.id.lt(cursorId) : null;


        // query 생성
        JPAQuery<PostingEntity> query
                = queryFactory.selectFrom(posting)
                .where(findConditionEqual
                        .and(idLessThanCursor));
        // find 정렬방식, 범위 설정
        if (scope.getOffset() != null)
            query.offset(scope.getOffset());
        if (scope.getLimit() != null)
            query.limit(scope.getLimit());
        if (scope.getOrderSpec() != null)
            query.orderBy(scope.getOrderSpec());


        // query 실행 및 결과 반환
        List<PostingEntity> foundEntities = query.fetch();

        return foundEntities;
    }


    private BooleanExpression equalsToFindCondition(QPostingEntity posting,
                                                    FindPostingsCondition findCondition) {

        BooleanExpression findConditionEqual
                = boardIdEq(posting, findCondition)
                .and(authorUuidEq(posting, findCondition))
                .and(softDeleteEq(posting, findCondition));
        return findConditionEqual;
    }

    private BooleanExpression boardIdEq(QPostingEntity posting,
                                        FindPostingsCondition condition) {

        return posting.boardId.eq(condition.getBoardId());
    }

    private BooleanExpression authorUuidEq(QPostingEntity posting,
                                           FindPostingsCondition condition) {

        return posting.authorUuid.eq(condition.getAuthorUuid());
    }

    private BooleanExpression softDeleteEq(QPostingEntity posting,
                                           FindPostingsCondition condition) {

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
