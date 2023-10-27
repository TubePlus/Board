package com.tubeplus.board_service.external.web.driving_adapter.board.vo;

import com.tubeplus.board_service.board.port.in.BoardUseCase;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Value
public class PutBoardReqBody {

    public static BoardUseCase.BoardProperty buildBoardSetting(PutBoardReqBody reqBody) {

        return null;
    }
}
