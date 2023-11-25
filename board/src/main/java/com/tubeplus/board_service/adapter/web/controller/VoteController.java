package com.tubeplus.board_service.adapter.web.controller;


import com.tubeplus.board_service.adapter.web.common.ApiResponse;
import com.tubeplus.board_service.adapter.web.common.ApiTag;
import com.tubeplus.board_service.adapter.web.controller.vo.vote.VoPostingVoteProperty;
import com.tubeplus.board_service.application.posting.domain.vote.Vote;
import com.tubeplus.board_service.application.posting.port.in.WebVoteUseCase;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "*")

@Validated
@ApiTag(path = "/api/v1/board-service/votes", name = "Posting vote API")
public class VoteController {

    private final WebVoteUseCase voteService;

    // all commands
    @GetMapping("/test")
    public ApiResponse<String> test() {
        return ApiResponse.ofSuccess("test");
    }

    @Operation(summary = "게시물 투표 api")
    @PostMapping()
    public ApiResponse<Long> postVote
            (
                    @Valid @RequestBody VoPostingVoteProperty voVoteProperty
            ) {

        Vote vote = voVoteProperty.buildNoneIdVote();

        Long postedVoteId
                = voteService.votePosting(vote);

        return ApiResponse.ofSuccess(postedVoteId);
    }


    @Operation(summary = "게시물 투표 수정 api")
    @PutMapping("/{id}")
    public ApiResponse<Long> modifyVote
            (
                    @Min(0) @PathVariable("id") Long voteId,
                    @Valid @RequestBody VoPostingVoteProperty voUpdateInfo
            ) {

        Vote updateInfo
                = voUpdateInfo.buildVote(voteId);

        final long voteCount
                = voteService.updateVote(updateInfo);

        return ApiResponse.ofSuccess(voteCount);
    }


    @Operation(summary = "게시물 투표 취소 api")
    @DeleteMapping("/{id}")
    public ApiResponse<Long> cancelVote
            (
                    @PathVariable("id") Long voteId
            ) {

        final long voteCount
                = voteService.deleteVote(voteId);

        return ApiResponse.ofSuccess(voteCount);
    }


}
