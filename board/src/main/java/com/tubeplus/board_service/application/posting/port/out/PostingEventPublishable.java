package com.tubeplus.board_service.application.posting.port.out;

import com.tubeplus.board_service.application.posting.domain.posting.Posting;

public interface PostingEventPublishable {
    void publishPostingRead(Posting posting);
}
