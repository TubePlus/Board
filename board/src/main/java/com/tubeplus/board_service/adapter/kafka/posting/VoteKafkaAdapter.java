package com.tubeplus.board_service.adapter.kafka.posting;

import com.tubeplus.board_service.adapter.web.error.ErrorCode;
import com.tubeplus.board_service.application.posting.domain.vote.Vote;
import com.tubeplus.board_service.application.posting.port.out.PostingPersistable;
import com.tubeplus.board_service.application.posting.port.out.VoteEventPublishable;
import com.tubeplus.board_service.adapter.kafka.common.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class VoteKafkaAdapter implements VoteEventPublishable {


    private final KafkaProducer kafkaProducer;


    @Override
    public void publishPostingVoted(PostingPersistable postingPersistence, Vote savedVote) {


        long votedPostingId
                = savedVote.getPostingId();

        Long communityId
                = postingPersistence.getPostingCommuId(votedPostingId)
                .ifExceptioned.thenThrow(ErrorCode.FIND_ENTITY_FAILED);

        Integer votePoint
                = savedVote.getVoteType().getCode();

        kafkaProducer.producerLikePosting(communityId, Long.valueOf(votePoint));

    }

    @Override
    public void publishVoteUpdated(PostingPersistable postingPersistence, Vote updateInfo) {

        long votedPostingId
                = updateInfo.getPostingId();

        Long communityId
                = postingPersistence.getPostingCommuId(votedPostingId)
                .ifExceptioned.thenThrow(ErrorCode.FIND_ENTITY_FAILED);

        kafkaProducer.producerLikePosting(communityId, 2 * Long.valueOf(updateInfo.getVoteType().getCode()));
    }


    @Override
    public void publishVoteDeleted(PostingPersistable postingPersistence, Vote deletedVote) {

        long postingId
                = deletedVote.getPostingId();

        Long communityId
                = postingPersistence.getPostingCommuId(postingId)
                .ifExceptioned.thenThrow(new RuntimeException("find vote-deleted-community id failed"));

        Long deletedVotePoint
                = Long.valueOf(deletedVote.getVoteType().getCode());

        kafkaProducer.producerLikePosting(communityId, (-1) * deletedVotePoint);

    }
}
