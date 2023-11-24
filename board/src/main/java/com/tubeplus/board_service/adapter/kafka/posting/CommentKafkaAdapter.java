package com.tubeplus.board_service.adapter.kafka.posting;

import com.tubeplus.board_service.adapter.rdb.posting.PostingPersistence;
import com.tubeplus.board_service.adapter.web.error.ErrorCode;
import com.tubeplus.board_service.application.posting.domain.comment.Comment;
import com.tubeplus.board_service.application.posting.port.out.CommentEventPublishable;
import com.tubeplus.board_service.adapter.kafka.common.KafkaProducer;
import com.tubeplus.board_service.application.posting.port.out.PostingPersistable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentKafkaAdapter
        implements CommentEventPublishable {

    private final KafkaProducer kafkaProducer;
    private final PostingPersistable postingPersistence;


    @Override
    public void publishCommented(Comment savedComment) {

        Long commentedPostingId
                = savedComment.getPostingId();

        Long commentedCommunityId
                = postingPersistence.getPostingCommuId(commentedPostingId)
                .ifExceptioned.thenThrow(ErrorCode.FIND_ENTITY_FAILED);

        try {
            kafkaProducer.producerPutComment(commentedCommunityId, 1L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
