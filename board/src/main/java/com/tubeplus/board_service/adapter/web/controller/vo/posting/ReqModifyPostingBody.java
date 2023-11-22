package com.tubeplus.board_service.adapter.web.controller.vo.posting;


import com.tubeplus.board_service.application.posting.port.in.PostingUseCase.ModifyArticleForm;
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

    private final boolean withImage;


    public ModifyArticleForm buildForm() {

        return ModifyArticleForm
                .builder()
                .userUuid(userUuid)
                .title(title)
                .contents(contents)
                .withImage(withImage)
                .build();
    }
}
