package com.tubeplus.board_service.application.posting.port.out;


import com.tubeplus.board_service.application.posting.domain.posting.Posting;
import com.tubeplus.board_service.application.posting.domain.vote.VoteType;
import com.tubeplus.board_service.global.Exceptionable;
import com.tubeplus.board_service.application.posting.domain.vote.Vote;
import lombok.Builder;
import lombok.Data;

import java.util.Optional;


public interface VotePersistable {

    Exceptionable<Boolean, Long> deleteVote(Long voteId);

    Exceptionable<Optional<Vote>, Long> findVote(Long voteId);

    Exceptionable<Optional<Vote>, FindVoteDto> findVote(FindVoteDto dto);

    @Data(staticConstructor = "of")
    class FindVoteDto {

        final Long postingId;
        final String voterUuid;
    }


    Exceptionable<Boolean, Vote> updateVote(Vote updateInfo);


    Exceptionable<Posting, Long> getVotedPosting(Long voteId);

    Exceptionable<Long, Long> getTotalVote(Long postingId);


    Exceptionable<Vote, SaveVoteDto> saveVote(SaveVoteDto dto);

    @Data
    @Builder
    class SaveVoteDto {
        private final long postingId;
        private final String voterUuid;
        private final VoteType voteType;

        public static SaveVoteDto builtFrom(Vote vote) {
            return SaveVoteDto.builder()
                    .postingId(vote.getPostingId())
                    .voterUuid(vote.getVoterUuid())
                    .voteType(vote.getVoteType())
                    .build();
        }
    }



}
