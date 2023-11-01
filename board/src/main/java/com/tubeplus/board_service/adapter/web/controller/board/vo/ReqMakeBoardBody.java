package com.tubeplus.board_service.adapter.web.controller.board.vo;

import com.tubeplus.board_service.board.domain.BoardType;
import com.tubeplus.board_service.board.port.in.BoardUseCase;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@Value
public class ReqMakeBoardBody {

    @Min(0)
    private long communityId;

    @NotBlank
    private String boardName;

    @NotBlank
    private BoardType boardType;

    @NotEmpty
    private String boardDescription;

    @Future
    private LocalDateTime limitDateTime;


    public BoardUseCase.MakeBoardForm buildForm() {
        log.info(this.toString());

        BoardUseCase.MakeBoardForm form
                = BoardUseCase.MakeBoardForm.builder().
                communityId(communityId).
                boardName(boardName).
                boardType(boardType).
                boardDescription(boardDescription).
                limitDateTime(limitDateTime).
                build();
        log.info(form.toString());

        return form;
    }
}
