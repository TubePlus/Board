package com.tubeplus.board_service.adapter.rdb.persistence.posting.dao;


import com.tubeplus.board_service.application.posting.port.out.PostingPersistent.UpdateWritingDto;

public interface PostingQDslRepositoryCustom {

    long updateSoftDelete(Long postingId);

    long updatePinReversed(Long postingId);

}
