package com.tubeplus.board_service.adapter.rdb.posting.dao;

import com.tubeplus.board_service.adapter.rdb.board.BoardEntity;
import com.tubeplus.board_service.adapter.rdb.posting.entity.VoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteJpaDataRepository extends JpaRepository<VoteEntity, Long> {
    VoteEntity findByPostingIdAndVoterUuid(Long postingId, String voterUuid);
}
