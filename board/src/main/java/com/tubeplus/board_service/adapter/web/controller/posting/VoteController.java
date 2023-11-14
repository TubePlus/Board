package com.tubeplus.board_service.adapter.web.controller.posting;


import com.tubeplus.board_service.adapter.web.common.ApiResponse;
import com.tubeplus.board_service.adapter.web.common.ApiTag;
import com.tubeplus.board_service.adapter.web.controller.posting.vo.vote.VoPostingVote;
import com.tubeplus.board_service.application.posting.domain.vote.Vote;
import com.tubeplus.board_service.application.posting.port.in.VoteUseCase;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/board-service/votes")
@CrossOrigin(origins = "*") //todo: 임시설정

@Validated
//@ApiTag(path = "/api/v1/votes", name = "Posting vote API")
public class VoteController {

    private final VoteUseCase voteService;

    @GetMapping("/test")
    public ApiResponse<String> test() {
        return ApiResponse.ofSuccess("test");
    }

    @Operation(summary = "게시물 투표 api")
    @PostMapping()
    public ApiResponse<Long> votePosting
            (
                    @Valid @RequestBody VoPostingVote voteVo
            ) {

        Vote vote
                = voteVo.buildVote();

        long voteCount
                = voteService.votePosting(vote);

        return ApiResponse.ofSuccess(voteCount);
    }


    @Operation(summary = "게시물 투표 수정 api")
    @PutMapping("/{voteId}")
    public ApiResponse<Long> modifyVote
            (
                    @Valid @RequestBody VoPostingVote voVote
            ) {

        Vote vote
                = voVote.buildVote();

        final long voteCount
                = voteService.modifyPostingVote(vote);

        return ApiResponse.ofSuccess(voteCount);
    }


    @Operation(summary = "게시물 투표 취소 api")
    @DeleteMapping("/{voteId}")
    public ApiResponse<Long> cancelVote() {

        long voteCount
                = voteService.cancelVote();

        return ApiResponse.ofSuccess(voteCount);
    }


}
