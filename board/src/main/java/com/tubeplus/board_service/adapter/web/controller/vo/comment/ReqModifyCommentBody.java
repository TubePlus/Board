package com.tubeplus.board_service.adapter.web.controller.vo.comment;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class ReqModifyCommentBody {

    @NotBlank
    private String content;
}
