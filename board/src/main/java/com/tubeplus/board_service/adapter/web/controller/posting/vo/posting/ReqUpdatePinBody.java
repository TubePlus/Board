package com.tubeplus.board_service.adapter.web.controller.posting.vo.posting;


import lombok.Value;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Value
public class ReqUpdatePinBody {
    private final boolean pin;
}
