package com.tubeplus.board_service.adapter.web.controller.vo.board;

import com.tubeplus.board_service.application.board.port.in.BoardUseCase.BoardProperty.TimeLimitBoardProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReqUpdateTimeLimitPropertyBody {

    private LocalDateTime limitDateTime;

    public TimeLimitBoardProperty toDomain() {
        return TimeLimitBoardProperty.of(limitDateTime);
    }
}
