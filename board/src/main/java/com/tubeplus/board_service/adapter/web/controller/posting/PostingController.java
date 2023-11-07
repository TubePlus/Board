package com.tubeplus.board_service.adapter.web.controller.posting;

import com.tubeplus.board_service.adapter.web.common.ApiResponse;
import com.tubeplus.board_service.adapter.web.common.ApiTag;
import com.tubeplus.board_service.adapter.web.controller.posting.vo.posting.ReqUpdatePinStateBody;
import com.tubeplus.board_service.adapter.web.controller.posting.vo.posting.*;
import com.tubeplus.board_service.adapter.web.error.BusinessException;
import com.tubeplus.board_service.adapter.web.error.ErrorCode;
import com.tubeplus.board_service.application.posting.domain.posting.Posting;
import com.tubeplus.board_service.application.posting.domain.posting.PostingView;
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
@ApiTag(path = "/api/v1/postings", name = "Posting API")
public class PostingController {


    private final PostingUseCase postingService;


    @Operation(summary = "게시물 작성", description = "작성된 게시물의 id 반환")
    @PostMapping
    public ApiResponse<Long> makePosting
            (
                    @RequestBody @Valid ReqMakePostingBody reqBody
            ) {

        MakePostingForm form = reqBody.buildForm();

        Long postedBoardId
                = postingService.makePosting(form);


        return ApiResponse.ofSuccess(postedBoardId);
    }


    @Operation(summary = "게시판내 게시물 목록 조회", description = "제목, 고정글 여부등의 간단한 정보 목록 조회")
    @GetMapping
    public ApiResponse<List<PostingSimpleInfo>> readPostingTitles//todo 요구사항 반영해 수정
    (
            @RequestParam("board_id") @Min(1) long boardId,
            @RequestParam("view_req_type") PostingsViewReqType viewReqType, //todo 나중에 필요한거 더 추가하기
            @RequestParam(name = "pin", required = false) Boolean pin,
            @RequestParam(value = "title_like", required = false) String titleLike
    ) {

        List<PostingSimpleInfo> postingSimpleInfos;
        switch (viewReqType) {

            case FEED -> postingSimpleInfos = postingService.feedPostingTitles(boardId, null);//todo null없애기

            case PAGE -> postingSimpleInfos = postingService.pagePostingTitles(boardId, null);

            default -> throw new BusinessException(ErrorCode.BAD_REQUEST);
        }

        return ApiResponse.ofSuccess(postingSimpleInfos);
    }


    @Operation(summary = "내 게시물 title 목록 읽어오기")
    @GetMapping("/mine")
    public ApiResponse<List<PostingSimpleInfo>> readMyPostingTitles
            (
                    @RequestParam("userUuid") @NotBlank String userUuid
            ) {

        List<PostingSimpleInfo> titleViews
                = postingService.readMyPostingTitles(userUuid);

        return ApiResponse.ofSuccess(titleViews);
    }


    @Operation(summary = "id로 게시물 읽기", description = "게시물 id로 개별 게시물 조회 및 읽기(조회수 반영등) 처리")
    @GetMapping("/{id}")
    public ApiResponse<PostingView> readPosting
            (
                    @PathVariable("id") @Min(1) long id,
                    @RequestParam("user-uuid") @NotBlank String userUuid
            ) {

        PostingView postingView
                = postingService.readPostingView(id, userUuid);

        return ApiResponse.ofSuccess(postingView);
    }


    @Operation(summary = "작성자가 게시물 수정",
            description = "게시물 작성자가 게시물을 수정할때 사용, 요청자가 작성자인지 권한 점검")
    @PutMapping("/{id}/article")
    public ApiResponse<PostingVo> modifyPostingArticle
            (
                    @PathVariable("id") @Min(1) long id,
                    @RequestBody @Valid ReqModifyPostingBody reqBody
            ) {

        ModifyArticleForm form
                = reqBody.buildForm();

        Posting modifiedPosting
                = postingService.modifyPostingArticle(id, form);

        return ApiResponse.ofSuccess
                (PostingVo.builtFrom(modifiedPosting));
    }


    @Operation(summary = "게시물 상단고정 상태 변경")
    @PutMapping("/{id}/pin-state")
    public ApiResponse modifyPostingPinState
            (
                    @PathVariable("id") @Min(1) long postingId,
                    @RequestBody @Valid ReqUpdatePinStateBody reqBody
            ) {

        ModifyPinStateInfo updateInfo
                = reqBody.buildUpdateInfoOf(postingId);

        postingService.modifyPostingPinState(updateInfo);

        return ApiResponse.ofSuccess(null);
    }


    @Operation(summary = "게시물 삭제(Soft Delete)")
    @DeleteMapping("/{id}")
    public ApiResponse softDeletePosting
            (
                    @PathVariable("id") @Min(1) long id
            ) {

        postingService.modifyDeletePosting(ModifySoftDeleteInfo.of(id, true));

        return ApiResponse.ofSuccess(null);
    }


}