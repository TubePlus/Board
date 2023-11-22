package com.tubeplus.board_service.adapter.rdb.posting.dao;

import com.tubeplus.board_service.adapter.rdb.board.BoardEntity;
import com.tubeplus.board_service.adapter.rdb.posting.entity.VoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteJpaDataRepository extends JpaRepository<VoteEntity, Long> {
    Optional<VoteEntity> findByPostingIdAndVoterUuid(Long postingId, String voterUuid);
}
