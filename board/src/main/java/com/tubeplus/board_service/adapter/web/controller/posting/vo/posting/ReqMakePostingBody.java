package com.tubeplus.board_service.adapter.web.controller.posting.vo.posting;


import com.tubeplus.board_service.posting.port.in.PostingUseCase;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Value
public class ReqMakePostingBody {

    @NotNull
    @Min(1)
    private final Long boardId;

    @NotBlank
    private final String authorUuid;

    @NotBlank
    private final String title;

    @NotEmpty
    private final String contents;

    public PostingUseCase.MakePostingForm buildForm() {
        return PostingUseCase.MakePostingForm
                .builder()
                .boardId(boardId)
                .authorUuid(authorUuid)
                .title(title)
                .contents(contents)
                .build();
    }
}
