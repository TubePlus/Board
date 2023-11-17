package com.tubeplus.board_service.adapter.rdb.persistence.posting;

import com.tubeplus.board_service.global.Exceptionable;
import com.tubeplus.board_service.application.posting.domain.vote.Vote;
import com.tubeplus.board_service.application.posting.domain.vote.VoteType;
import com.tubeplus.board_service.application.posting.port.out.VotePersistable;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component("votePersistence")
public class VotePersistence implements VotePersistable {

    @Override
    public Exceptionable <Optional<Vote>, FindVoteDto> findVote(FindVoteDto dto) {

        return new Exceptionable<>(this::findUserVoteOfPosting, dto);
    }

    protected Optional<Vote> findUserVoteOfPosting(FindVoteDto dto) {
        Vote v = Vote.builder().id(2).voterUuid("aslkdfdsalj").postingId(0).voteType(VoteType.HATE).build();
        return Optional.of(v);//todo 테스트용
    }

}
