package com.tubeplus.board_service.adapter.rdb.persistence.posting.dao;


import com.querydsl.core.types.OrderSpecifier;
import com.tubeplus.board_service.adapter.rdb.persistence.posting.PostingEntity;
import com.tubeplus.board_service.application.posting.port.out.PostingPersistent;
import com.tubeplus.board_service.application.posting.port.out.PostingPersistent.FindPostingsCondition;
import com.tubeplus.board_service.application.posting.port.out.PostingPersistent.PagePostingsDto;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface PostingQDslRepositoryCustom {

    @Transactional(readOnly = true)
    Page<PostingEntity> pagePostingEntities(FindPostingsCondition findCondition,
                                            PageRequest pageReq,
                                            OrderSpecifier orderSpec);

    Page<PostingEntity> pagePostingEntities(PagePostingsDto dto);


    @Transactional(readOnly = true)
    Long countPostingEntities(FindPostingsCondition findCondition);


    @Transactional(readOnly = true)
    List<PostingEntity> findPostingEntities(FindPostingsCondition findCondition,
                                            SortScope scope,
                                            Long cursorId);

    @Data(staticConstructor = "of")
    class SortScope {
        private final Integer limit;
        private final Long offset;
        private final OrderSpecifier orderSpec;

        public static SortScope of(PageRequest pageReq,
                                   OrderSpecifier orderSpec) {

            return SortScope.of(pageReq.getPageSize(), pageReq.getOffset(), orderSpec);
        }

        public static SortScope of(PageRequest pageReq) {

            return SortScope.of(pageReq.getPageSize(), pageReq.getOffset(), null);
        }

        public static SortScope NotSpecified() {

            return SortScope.of(0, 0L, null);
        }
    }


//    long updateSoftDelete(Long postingId);


}
