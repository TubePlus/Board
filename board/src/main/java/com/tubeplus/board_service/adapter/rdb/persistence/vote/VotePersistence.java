package com.tubeplus.board_service.adapter.rdb.persistence.vote;

import com.tubeplus.board_service.global.Exceptionable;
import com.tubeplus.board_service.posting.domain.vote.Vote;
import com.tubeplus.board_service.posting.domain.vote.VoteType;
import com.tubeplus.board_service.posting.port.out.VotePersistent;
import org.checkerframework.checker.nullness.Opt;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class VotePersistence implements VotePersistent {

    @Override
    public Exceptionable
            <Optional<Vote>, FindVoteDto> findVote(FindVoteDto dto) {

        return new Exceptionable<>(this::findUserVoteOfPosting, dto);
    }

    protected Optional<Vote> findUserVoteOfPosting(FindVoteDto dto) {
        Vote v = Vote.builder().id(2).voterUuid("aslkdfdsalj").postingId(0).voteType(VoteType.HATE).build();
        return Optional.of(v);//todo 테스트용
    }

}
