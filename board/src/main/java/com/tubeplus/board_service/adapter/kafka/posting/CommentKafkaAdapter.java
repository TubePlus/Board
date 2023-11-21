package com.tubeplus.board_service.adapter.kafka.posting;

import com.tubeplus.board_service.application.posting.port.out.CommentEventPublishable;
import com.tubeplus.board_service.adapter.kafka.common.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentKafkaAdapter
        implements CommentEventPublishable {

    private final KafkaProducer kafkaProducer;

    @Override
    public void publishCommented(Long communityId) {
        try {
            kafkaProducer.producerPutComment(communityId, 1L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
