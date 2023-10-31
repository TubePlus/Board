package com.tubeplus.board_service.adapter.web.controller.posting.vo.vote;


import com.tubeplus.board_service.posting.domain.PostingVote;
import com.tubeplus.board_service.posting.domain.PostingVoteType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Value
public class VoPostingVote {

    @NotNull
    @Min(1)
    private final Long postingId;

    @NotBlank
    private final String voterUuid;

    @NotNull
    private final PostingVoteType voteType;


    public PostingVote buildVote() {
        return PostingVote
                .builder()
                .postingId(postingId)
                .voterUuid(voterUuid)
                .voteType(voteType)
                .build();
    }
}
