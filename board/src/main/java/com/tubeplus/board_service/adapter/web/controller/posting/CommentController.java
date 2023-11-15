package com.tubeplus.board_service.adapter.web.controller.posting;


import com.tubeplus.board_service.adapter.web.common.ApiResponse;
import com.tubeplus.board_service.adapter.web.controller.posting.vo.comment.ReqModifyCommentBody;
import com.tubeplus.board_service.adapter.web.controller.posting.vo.comment.ReqPostCommentBody;
import com.tubeplus.board_service.application.posting.domain.comment.Comment;
import com.tubeplus.board_service.application.posting.domain.comment.Comment.CommentViewInfo;
import com.tubeplus.board_service.application.posting.port.in.CommentUseCase;
import com.tubeplus.board_service.application.posting.port.in.CommentUseCase.PostCommentForm;
import com.tubeplus.board_service.application.posting.port.in.CommentUseCase.ReadCommentDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/board-service/comments")
@CrossOrigin(origins = "*") //todo: 임시설정

@Validated
//@ApiTag(path = "/api/v1/comments", name = "Posting Comment API")
public class CommentController {

    private final CommentUseCase commentService;

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @Operation(summary = "댓글/대댓글 작성"
            , description = "대댓글일경우 parentId를 입력, 원 댓글일 경우 parentId에 null")
    @PostMapping()
    public ApiResponse<Long> postComment
            (
                    @Valid @RequestBody
                    ReqPostCommentBody reqBody
            ) {

        PostCommentForm form
                = reqBody.buildCommentForm();

        Long postedCommentId
                = commentService.writeComment(form);

        return ApiResponse.ofSuccess(postedCommentId);
    }


    @Operation(summary = "댓글/대댓글 조회",
            description = "대댓글일 경우 parentId를 입력, 원 댓글일 경우 parentId에 null")
    @GetMapping()
    public ApiResponse<CommentViewInfo> readComment
            (
                    @RequestParam("posting_id") @Min(1)
                    long postingId,
                    @RequestParam(name = "parent_id", required = false) @Min(1)
                    Long parentId
            ) {

        ReadCommentDto dto
                = ReadCommentDto
                .builder()
                .postingId(postingId)
                .parentId(parentId)
                .build();

        CommentViewInfo viewInfo
                = commentService.readComment(dto);

        return ApiResponse.ofSuccess(viewInfo);
    }


    @Operation(summary = "댓글/대댓글 수정")
    @PutMapping("/{commentId}")
    public ApiResponse<Comment.CommentViewInfo> modifyComment
            (
                    @PathVariable("commentId") @Min(1)
                    long commentId,
                    @Valid @RequestBody
                    ReqModifyCommentBody reqBody
            ) {

        Comment.CommentViewInfo viewInfo
                = commentService.modifyComment
                (commentId, reqBody.getContents());

        return ApiResponse.ofSuccess(viewInfo);
    }


    @Operation(summary = "댓글/대댓글 삭제", description = "대댓글있을경우 cascade 삭제")
    @DeleteMapping("/{commentId}")
    public ApiResponse deleteComment
            (
                    @PathVariable("commentId") @Min(1)
                    long idToDelete
            ) {

        commentService.deleteComment(idToDelete);

        return ApiResponse.ofSuccess(null);
    }


}
