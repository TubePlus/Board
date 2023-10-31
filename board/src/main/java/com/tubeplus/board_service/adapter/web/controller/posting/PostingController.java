package com.tubeplus.board_service.adapter.web.controller.posting;

import com.tubeplus.board_service.adapter.web.common.ApiResponse;
import com.tubeplus.board_service.adapter.web.common.ApiTag;
import com.tubeplus.board_service.adapter.web.controller.posting.vo.*;
import com.tubeplus.board_service.adapter.web.error.BusinessException;
import com.tubeplus.board_service.adapter.web.error.ErrorCode;
import com.tubeplus.board_service.posting.domain.Posting;
import com.tubeplus.board_service.posting.domain.PostingView;
import com.tubeplus.board_service.posting.port.in.PostingUseCase;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor

@Validated
@ApiTag(path = "/api/v1/postings", name = "PostingView API", description = "게시물 CRUD API")
public class PostingController {


    private final PostingUseCase postingService;


    @Operation(summary = "게시물 작성", description = "게시물 작성 api, 작성된 게시물의 id 반환")
    @PostMapping("")
    public ApiResponse<Long> makePosting
            (
                    @Valid @RequestBody MakePostingReqBody reqBody
            ) {

        PostingUseCase
                .MakePostingForm form
                = reqBody.buildForm();

        Long postedBoardId
                = postingService.makePosting(form);


        return ApiResponse.ofSuccess(postedBoardId);
    }


    @Operation(summary = "게시판내 게시물 목록 조회", description = "특정 id의 게시판내 게시물들 제목, 고정글 여부등의 간단한 정보 목록 조회")
    @GetMapping("")
    public ApiResponse<List<PostingTitleVo>> readPostingTitles
            (
                    @RequestParam("board_id") @Min(1) long boardId,
                    @RequestParam("page_type") PostingPageType pageType, //todo 나중에 필요한거 더 추가하기
                    @RequestParam(name = "pin", required = false) Boolean pin,
                    @RequestParam(value = "title_like", required = false) String titleLike
            ) {


        List<PostingUseCase.PostingTitle> titles;
        switch (pageType) {

            case FEED -> titles = postingService.feedPostingTitles(boardId, null);//todo null없애기

            case LIST -> titles = postingService.pagePostingTitles(boardId, null);

            default -> throw new BusinessException(ErrorCode.BAD_REQUEST);

        }


        List<PostingTitleVo> titleVoList
                = titles.stream()
                .map(
                        PostingTitleVo::builtFrom
                ).collect(Collectors.toList());

        return ApiResponse.ofSuccess(titleVoList);
    }


    @Operation(summary = "id로 게시물 읽기", description = "게시물 id로 개별 게시물 조회 및 읽기 처리")
    @GetMapping("/{postingId}")
    public ApiResponse<PostingView> readPosting
            (
                    @PathVariable("postingId") @Min(1) long id
            ) {

        PostingView postingView
                = postingService.viewPosting(id);

        return ApiResponse.ofSuccess(postingView);
    }


    @Operation(summary = "게시물 수정", description = "게시물 id로 지정된 게시물을 상단 고정된 상태로 저장")
    @PutMapping("/{postingId}")
    public ApiResponse<PostingVo> modifyPosting
            (
                    @Valid @RequestBody ModifyPostingReqBody reqBody
            ) {

        PostingUseCase
                .ModifyPostingForm form
                = reqBody.buildForm();


        Posting modifiedPosting
                = postingService.modifyPosting(form);


        return ApiResponse.ofSuccess
                (
                        PostingVo.builtFrom(modifiedPosting)
                );

    }


    @Operation(summary = "게시물 고정", description = "게시물 id로 지정된 게시물을 상단 고정된 상태로 저장")
    @PostMapping("/{postingId}/pinned")
    public ApiResponse pinPosting
            (
                    @PathVariable("postingId") @Min(1) long id
            ) {

        postingService.pinPosting(id);

        return ApiResponse.ofSuccess(null);
    }


    @Operation(summary = "게시물 삭제", description = "게시물 id로 지정된 게시물을 삭제")
    @DeleteMapping("/{postingId}")
    public ApiResponse softDeletePosting
            (
                    @PathVariable("postingId") @Min(1) long id
            ) {


        postingService.softDeletePosting(id);

        return ApiResponse.ofSuccess(null);
    }



    //todo commentController 따로 분리
//
//    @PostMapping("/{postingId}/votes")
//    public ApiResponse votePosting
//            (
//                    @Valid @RequestBody VoteReqBody reqBody
//            ) {
//
//    }
////    @Operation(summary = "댓글/대댓글 작성", description = "대댓글일경우 parentId를 입력, 원 댓글일 경우 parentId에 null")
//    @PostMapping("/{postingId}")
//    public ApiResponse softDeletePosting
//            (
//                    @PathVariable
//            )
}