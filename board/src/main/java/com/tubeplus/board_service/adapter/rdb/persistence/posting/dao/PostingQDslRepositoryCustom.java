package com.tubeplus.board_service.adapter.rdb.persistence.posting.dao;



public interface PostingQDslRepositoryCustom {

    long updateSoftDelete(Long postingId);

    long updatePostingPinned(Long postingId);
}
