package com.tubeplus.board_service.adapter.rdb.persistence.posting.dao;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.tubeplus.board_service.adapter.rdb.persistence.posting.QPostingEntity;
import junit.framework.TestCase;
import org.junit.Test;

public class PostingQDslRepositoryImplTest extends TestCase {

    @Test
    public void test() {
        QPostingEntity posting = QPostingEntity.postingEntity;

        BooleanExpression booleanExpression = posting.id.eq(1L).and(null);

    }

}