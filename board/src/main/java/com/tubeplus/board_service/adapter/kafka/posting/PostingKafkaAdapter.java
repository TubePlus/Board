package com.tubeplus.board_service.adapter.kafka.posting;

import com.tubeplus.board_service.application.posting.domain.posting.Posting;
import com.tubeplus.board_service.application.posting.port.out.PostingEventPublishable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class PostingKafkaAdapter implements PostingEventPublishable {
    @Override
    public void publishPostingRead(Posting posting) {

    }
}
