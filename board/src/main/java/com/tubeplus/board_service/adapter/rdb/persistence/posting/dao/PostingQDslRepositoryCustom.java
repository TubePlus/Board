package com.tubeplus.board_service.adapter.rdb.persistence.posting.dao;


import com.tubeplus.board_service.adapter.rdb.persistence.posting.PostingEntity;
import com.tubeplus.board_service.application.posting.port.out.PostingPersistent.FindPostingsDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface PostingQDslRepositoryCustom {


    @Transactional(readOnly = true)
    Long countPostingEntities(FindPostingsDto.ConditionByFields whereCondition);


    @Transactional(readOnly = true)
    List<PostingEntity> findPostingEntities(FindPostingsDto findDto);


}
