package com.tubeplus.board_service.adapter.web.controller.posting.vo.posting;


import com.tubeplus.board_service.posting.port.in.PostingUseCase;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;



@Value
public class ReqModifyPostingBody {

    @NotBlank
    private final String userUuid;

    @NotBlank
    private final String title;

    @NotBlank
    private final String contents;


    public PostingUseCase.ModifyPostingForm buildForm() {

        return PostingUseCase.ModifyPostingForm
                .builder()
                .userUuid(userUuid)
                .title(title)
                .contents(contents)
                .build();
    }
}