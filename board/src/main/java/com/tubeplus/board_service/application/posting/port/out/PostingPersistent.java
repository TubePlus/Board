package com.tubeplus.board_service.application.posting.port.out;


import com.tubeplus.board_service.global.Exceptionable;
import com.tubeplus.board_service.application.posting.domain.posting.Posting;

import java.util.Optional;

public interface PostingPersistent {

    Exceptionable
            <Optional<Posting>, Long> findPosting(long postingId);

    Exceptionable
            <Boolean, Long> softDeletePosting(long postingId);

    Exceptionable
            <Boolean, Long> changePinState(long id);
}
