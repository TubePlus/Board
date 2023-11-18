package com.tubeplus.board_service.adapter.rdb.posting.dao;


import com.tubeplus.board_service.adapter.rdb.posting.entity.PostingEntity;
import com.tubeplus.board_service.application.posting.port.out.PostingPersistable.FindPostingsDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface PostingQDslRepositoryCustom {


    @Transactional(readOnly = true)
    Long countPostingEntities(FindPostingsDto.FieldsFindCondition whereCondition);


    @Transactional(readOnly = true)
    List<PostingEntity> findPostingEntities(FindPostingsDto findDto);


    @Transactional(readOnly = true)
    boolean existNextPosting(FindPostingsDto findDto);
}
