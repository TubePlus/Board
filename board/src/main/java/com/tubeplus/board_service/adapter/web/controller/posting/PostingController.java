package com.tubeplus.board_service.adapter.web.controller.posting;

import com.tubeplus.board_service.adapter.web.common.ApiResponse;
import com.tubeplus.board_service.adapter.web.common.ApiTag;
import com.tubeplus.board_service.adapter.web.controller.posting.vo.posting.*;
import com.tubeplus.board_service.adapter.web.error.BusinessException;
import com.tubeplus.board_service.adapter.web.error.ErrorCode;
import com.tubeplus.board_service.application.posting.domain.posting.Posting;
import com.tubeplus.board_service.application.posting.domain.posting.PostingViewInfo;
import com.tubeplus.board_service.application.posting.port.in.PostingUseCase;
import com.tubeplus.board_service.application.posting.port.in.PostingUseCase.MakePostingForm;
import com.tubeplus.board_service.application.posting.port.in.PostingUseCase.PostingSimpleInfo;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.tubeplus.board_service.application.posting.port.in.PostingUseCase.*;


@Slf4j
@RequiredArgsConstructor

@Validated
@ApiTag(path = "/api/v1/postings", name = "Posting API", description = "게시물 CRUD API")
public class PostingController {


    private final PostingUseCase postingService;


    @Operation(summary = "게시물 작성", description = "게시물 작성 api, 작성된 게시물의 id 반환")
    @PostMapping
    public ApiResponse<Long> makePosting
            (
                    @Valid @RequestBody
                            ReqMakePostingBody reqBody
            ) {

        MakePostingForm form
                = reqBody.buildForm();

        Long postedBoardId
                = postingService.makePosting(form);


        return ApiResponse.ofSuccess(postedBoardId);
    }


    @Operation(summary = "게시판내 게시물 목록 조회", description = "특정 id의 게시판내 게시물들 제목, 고정글 여부등의 간단한 정보 목록 조회")
    @GetMapping
    public ApiResponse<List<PostingSimpleInfo>> readPostingTitles//todo 요구사항 반영해 수정
    (
            @RequestParam("board_id")
            @Min(1) long boardId,

            @RequestParam("page_type")
                    PostingPageType pageType, //todo 나중에 필요한거 더 추가하기

            @RequestParam(name = "pin", required = false)
                    Boolean pin,

            @RequestParam(value = "title_like", required = false)
                    String titleLike
    ) {

        List<PostingSimpleInfo> titleViews;
        switch (pageType) {

            case FEED -> titleViews = postingService.feedPostingTitles(boardId, null);//todo null없애기

            case LIST -> titleViews = postingService.pagePostingTitles(boardId, null);

            default -> throw new BusinessException(ErrorCode.BAD_REQUEST);
        }

        return ApiResponse.ofSuccess(titleViews);
    }


    @Operation(summary = "내 게시물 title 목록 읽어오기")
    @GetMapping("/mine")
    public ApiResponse<List<PostingSimpleInfo>> readMyPostingTitles
            (
                    @RequestParam("userUuid")
                    @NotBlank String userUuid
            ) {

        List<PostingSimpleInfo> titleViews
                = postingService.readMyPostingTitles(userUuid);

        return ApiResponse.ofSuccess(titleViews);
    }


    @Operation(summary = "id로 게시물 읽기", description = "게시물 id로 개별 게시물 조회 및 읽기 처리")
    @GetMapping("/{postingId}")
    public ApiResponse<PostingViewInfo> readPosting
            (
                    @PathVariable("postingId")
                    @Min(1) long id,

                    @RequestParam("user-uuid")
                    @NotBlank String userUuid
            ) {

        PostingViewInfo viewInfo
                = postingService.readPosting(id, userUuid);

        return ApiResponse.ofSuccess(viewInfo);
    }


    @Operation(summary = "게시물 수정", description = "게시물 id로 지정된 게시물을 상단 고정된 상태로 저장")
    @PutMapping("/{postingId}")
    public ApiResponse<VoPosting> modifyPosting
            (
                    @Valid @RequestBody
                            ReqModifyPostingBody reqBody
            ) {

        ModifyPostingForm form
                = reqBody.buildForm();


        Posting modifiedPosting
                = postingService.modifyPosting(form);


        return ApiResponse.ofSuccess
                (
                        VoPosting.builtFrom(modifiedPosting)
                );

    }


    @Operation(summary = "게시물 고정", description = "게시물 id로 지정된 게시물을 상단 고정된 상태로 저장")
    @PostMapping("/{postingId}/change-pin-state")
    public ApiResponse changePinState
            (
                    @PathVariable("postingId")
                    @Min(1) long id
            ) {

        postingService.changePinState(id);

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


}