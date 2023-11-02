//package com.tubeplus.board_service.adapter.web.controller.posting;
//
//
//import com.tubeplus.board_service.adapter.web.common.ApiResponse;
//import com.tubeplus.board_service.adapter.web.common.ApiTag;
//import com.tubeplus.board_service.adapter.web.controller.posting.vo.vote.VoPostingVote;
//import com.tubeplus.board_service.application.posting.domain.vote.Vote;
//import com.tubeplus.board_service.application.posting.port.in.VoteUsecase;
//import io.swagger.v3.oas.annotations.Operation;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//
//
////@Slf4j
////@RequiredArgsConstructor
////
////@Validated
////@ApiTag(path = "/api/v1/votes", name = "Posting vote API", description = "게시물 투표 관련 CRUD API")
//public class VoteController {
//
//
////    private final VoteUsecase voteService;
//
//
//    @Operation(summary = "게시물 투표 api")
//    @PostMapping()
//    public ApiResponse<Long> votePosting
//            (
//                    @Valid @RequestBody VoPostingVote voteVo
//            ) {
//
//        Vote voteInfo
//                = voteVo.buildVote();
//
//        long voteCount
//                = voteService.votePosting(voteInfo);
//
//        return ApiResponse.ofSuccess(voteCount);
//    }
//
//
//    @Operation(summary = "게시물 투표 수정 api")
//    @PutMapping("/{voteId}")
//    public ApiResponse<Long> modifyVote
//            (
//                    @Valid @RequestBody VoPostingVote voVote
//            ) {
//
//        Vote vote
//                = voVote.buildVote();
//
//        final long voteCount
//                = voteService.modifyPostingVote(vote);
//
//        return ApiResponse.ofSuccess(voteCount);
//    }
//
//
//    @Operation(summary = "게시물 투표 취소 api")
//    @DeleteMapping("/{voteId}")
//    public ApiResponse<Long> cancelVote() {
//
//        long voteCount
//                = voteService.cancelVote();
//
//        return ApiResponse.ofSuccess(voteCount);
//    }
//
//
//}
