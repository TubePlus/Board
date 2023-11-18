package com.tubeplus.board_service.adapter.web.controller.vo.vote;


import com.tubeplus.board_service.application.posting.domain.vote.Vote;
import com.tubeplus.board_service.application.posting.domain.vote.VoteType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Value
public class VoPostingVoteProperty {

    @NotNull
    @Min(1)
    private final Long postingId;

    @NotBlank
    private final String voterUuid;

    @NotNull
    private final VoteType voteType;


    public Vote buildNoneIdVote() {

        return Vote
                .builder()
                .postingId(postingId)
                .voterUuid(voterUuid)
                .voteType(voteType)
                .build();
    }

    public Vote buildVote(Long voteId) {

        return Vote
                .builder()
                .id(voteId)
                .postingId(postingId)
                .voterUuid(voterUuid)
                .voteType(voteType)
                .build();
    }
}
