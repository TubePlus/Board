package com.tubeplus.board_service.adapter.web.controller.board.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tubeplus.board_service.application.board.port.in.BoardUseCase;
import com.tubeplus.board_service.application.board.port.in.BoardUseCase.BoardProperty.TimeLimitBoardProperty;
import lombok.Data;
import lombok.NonNull;
import lombok.Value;

import java.time.LocalDateTime;

@Data
public class ReqUpdateTimeLimitPropertyBody {

    @NonNull
    private LocalDateTime limitDateTime;

    public TimeLimitBoardProperty toDomain() {
        return TimeLimitBoardProperty.of(limitDateTime);
    }
}
