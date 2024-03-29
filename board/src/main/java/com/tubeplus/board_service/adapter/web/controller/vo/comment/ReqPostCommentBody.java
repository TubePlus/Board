package com.tubeplus.board_service.adapter.web.controller.vo.comment;


import com.tubeplus.board_service.application.posting.port.in.WebCommentUseCase.PostCommentForm;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;


@Value
@Slf4j
public class ReqPostCommentBody {

    @NotNull
    @Min(1)
    private Long postingId;

    @Min(1)
    private Long parentId;

    @NotBlank
    private String commenterUuid;

    @NotBlank
    private String contents;


    public PostCommentForm buildCommentForm() {

        return PostCommentForm
                .builder()
                .postingId(postingId)
                .parentId(parentId)
                .commenterUuid(commenterUuid)
                .contents(contents)
                .build();
    }
}
