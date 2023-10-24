package com.tubeplus.board_service.domain.board.model;

import com.tubeplus.board_service.global.base.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@AllArgsConstructor
public enum BoardType implements BaseEnum<String, String> {

    NORMAL("N"),
    MEMBERSHIP("M");


    private String code;

    @Override
    public String getCode() {
        log.info("hihi");

        return this.code;
    }

    @Override
    public String getValue() {
        return super.name();
    }


}
