package com.tubeplus.board_service.adapter.rdb.persistence.posting.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.tubeplus.board_service.adapter.rdb.persistence.posting.QPostingEntity;
import com.tubeplus.board_service.application.posting.port.out.PostingPersistent;
import com.tubeplus.board_service.application.posting.port.out.PostingPersistent.UpdateWritingDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Repository
@RequiredArgsConstructor
public class PostingQDslRepositoryImpl implements PostingQDslRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    @Transactional
    public long updateSoftDelete(Long postingId) {

        QPostingEntity posting
                = QPostingEntity.postingEntity;

        long updatedColumns =
                queryFactory.update(posting)
                        .where(posting.id.eq(postingId))
                        .set(posting.erase, true)
                        .execute();

        return updatedColumns;
    }

    @Override
    @Transactional
    public long updatePinReversed(Long postingId) {

        QPostingEntity posting
                = QPostingEntity.postingEntity;

        long updatedColumns =
                queryFactory.update(posting)
                        .where(posting.id.eq(postingId))
                        .set(posting.pin, posting.pin.not())
                        .execute();

        return updatedColumns;
    }

//    public long updatePostingWriting(UpdateWritingDto dto) {
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
