package com.tubeplus.board_service.posting.port.out;


import com.tubeplus.board_service.global.Exceptionable;
import com.tubeplus.board_service.posting.domain.vote.Vote;
import lombok.Data;

import java.util.Optional;


public interface VotePersistent {

    Exceptionable<Optional<Vote>, FindVoteDto> findVote(FindVoteDto dto);

    @Data
    class FindVoteDto {
        final long postingId;
        final String voterUuid;
    }

}
