package com.tubeplus.board_service.posting.port.out;


import com.tubeplus.board_service.global.Exceptionable;
import com.tubeplus.board_service.posting.domain.posting.Posting;

import java.util.Optional;

public interface PostingPersistent {

    Exceptionable<Optional<Posting>, Long> findPosting(long postingId);


}
