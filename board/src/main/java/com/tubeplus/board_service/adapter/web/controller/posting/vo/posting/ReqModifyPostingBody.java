package com.tubeplus.board_service.adapter.web.controller.posting.vo.posting;


import com.tubeplus.board_service.posting.port.in.PostingUseCase;
import com.tubeplus.board_service.posting.port.in.PostingUseCase.ModifyPostingForm;
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


    public ModifyPostingForm buildForm() {

        return ModifyPostingForm
                .builder()
                .userUuid(userUuid)
                .title(title)
                .contents(contents)
                .build();
    }
}
