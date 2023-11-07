package com.tubeplus.board_service.adapter.rdb.persistence.posting.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tubeplus.board_service.adapter.rdb.persistence.posting.QPostingEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Repository
@RequiredArgsConstructor
public class PostingQDslRepositoryImpl implements PostingQDslRepositoryCustom {

    private final JPAQueryFactory queryFactory;


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
}
