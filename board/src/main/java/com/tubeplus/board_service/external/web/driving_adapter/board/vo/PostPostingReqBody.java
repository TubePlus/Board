package com.tubeplus.board_service.external.web.driving_adapter.board.vo;

import com.tubeplus.board_service.domain.board.model.BoardType;
import com.tubeplus.board_service.domain.board.port.in.BoardUseCase;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@Value
public class PostPostingReqBody {

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


    public BoardUseCase.FormToMakeBoard buildFormToMakeBoard() {
        log.info(this.toString());

        BoardUseCase.FormToMakeBoard form
                = BoardUseCase.FormToMakeBoard.builder().
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
