package com.tubeplus.board_service.application.posting.port.out;


import com.tubeplus.board_service.global.Exceptionable;
import com.tubeplus.board_service.application.posting.domain.vote.Vote;
import lombok.Data;

import java.util.Optional;


public interface VotePersistent {

    Exceptionable<Optional<Vote>, FindVoteDto> findVote(FindVoteDto dto);

    @Data(staticConstructor = "of")
    class FindVoteDto {
        final Long postingId;
        final String voterUuid;
    }

}
