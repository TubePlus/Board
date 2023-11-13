package com.tubeplus.board_service.adapter.web.controller.posting;

import com.tubeplus.board_service.adapter.web.common.ApiResponse;
import com.tubeplus.board_service.adapter.web.common.ApiTag;
import com.tubeplus.board_service.adapter.web.controller.posting.vo.VoReadPostingSimpleData;
import com.tubeplus.board_service.adapter.web.controller.posting.vo.posting.ReqUpdatePinStateBody;
import com.tubeplus.board_service.adapter.web.controller.posting.vo.posting.*;
import com.tubeplus.board_service.adapter.web.error.BusinessException;
import com.tubeplus.board_service.adapter.web.error.ErrorCode;
import com.tubeplus.board_service.application.posting.domain.posting.Posting;
import com.tubeplus.board_service.application.posting.domain.posting.PostingView;
import com.tubeplus.board_service.application.posting.port.in.PostingUseCase;
import com.tubeplus.board_service.application.posting.port.in.PostingUseCase.MakePostingForm;
import com.tubeplus.board_service.application.posting.port.in.PostingUseCase.PostingSimpleData;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.tubeplus.board_service.application.posting.port.in.PostingUseCase.*;


@Slf4j
@RequiredArgsConstructor

@Validated
@ApiTag(path = "/api/v1/postings", name = "Posting API")
public class PostingController {


    private final PostingUseCase postingService;

    @GetMapping("/test")
    public ApiResponse<String> test() {
        return ApiResponse.ofSuccess("test");
    }

    @Operation(summary = "게시물 작성", description = "작성된 게시물의 id 반환")
    @PostMapping()
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
    @GetMapping()
    public ApiResponse<VoReadPostingSimpleData.Res> readPostingSimpleData
            (
                    // 검색 조건
                    // 주요 검색 조건 - searchReqType에 따라 필수 요청이 될 수 있는 파라미터
                    @RequestParam(name = "search-req-type", required = true) PostingsSearchReqType searchReqType,
                    @RequestParam(name = "board-id", required = false) Long boardId,
                    @RequestParam(name = "author-uuid", required = false) String authorUuid,
                    @RequestParam(name = "title-containing", required = false) String titleContaining,
                    @RequestParam(name = "content-containing", required = false) String contentContaining,
                    // 부가 조건 - 항상 필수 요청이 아닌 파라미터
                    @RequestParam(name = "pin", required = false) Boolean pin,
                    @RequestParam(name = "deleted", required = false) Boolean deleted,

                    // 요청된 화면표시 타입 관련
                    @RequestParam("view-req-type") PostingsViewReqType viewReqType, //todo AllArgsConstructor 있는 클래스로 받아서 한번에 처리
                    // pagination 요청시
                    @RequestParam(name = "page-index", required = false) Integer pageIndex,
                    @RequestParam(name = "page-size", required = false) Integer pageSize,
                    // feed(무한스크롤) 요청시
                    @RequestParam(name = "cursor-id", required = false) @Min(1) Long cursorId,
                    @RequestParam(name = "feed-size", required = false) Byte feedSize
            ) {

        // todo searchType enum 받아서 요청 타입별로 reqParam 유효성 조건점검 로직 깔끔하게 하기


        SearchPostingsInfo searchInfo //todo request param을 객체로 받은 후, requestParam 객체를 이용해서 searchPostingInfo 객체 생성
                = SearchPostingsInfo.builder().boardId(boardId).authorUuid(authorUuid).pin(pin).titleContaining(titleContaining).contentsContaining(contentContaining).softDelete(deleted).build();

        VoReadPostingSimpleData.Res responseVo;

        switch (viewReqType) {

            case FEED -> responseVo = VoReadPostingSimpleData.Res.of(
                    feedPostingData(searchInfo, FeedRequest.of(cursorId, feedSize)));

            case PAGE -> responseVo = VoReadPostingSimpleData.Res.of(
                    pagePostingData(searchInfo, PageRequest.of(pageIndex, pageSize)));

            default -> throw new BusinessException(ErrorCode.BAD_REQUEST, "view-req-type이 잘못되었습니다.");
        }

        return ApiResponse.ofSuccess(responseVo);
    }

    private Feed<PostingSimpleData> feedPostingData(SearchPostingsInfo searchInfo, FeedRequest feedReq) {

        InfoToFeedPostingData infoToFeed
                = InfoToFeedPostingData.of(searchInfo, feedReq);

        Feed<PostingSimpleData> postingDataFeed;
        postingDataFeed = postingService.feedPostingSimpleData(infoToFeed);

        return postingDataFeed;
    }

    private Page<PostingSimpleData> pagePostingData(SearchPostingsInfo searchInfo, PageRequest pageReq) {

        InfoToPagePostingData infoToPage
                = InfoToPagePostingData.of(searchInfo, pageReq);

        Page<PostingSimpleData> pagedPostingData
                = postingService.pagePostingSimpleData(infoToPage);

        return pagedPostingData;
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
