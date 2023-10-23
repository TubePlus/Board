package com.tubeplus.board_service.domain.board.model;

import com.tubeplus.board_service.global.base.BaseEnum;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum BoardType implements BaseEnum<String, String> {

    NORMAL("N"),
    MEMBERSHIP("M");


    private String code;

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getValue() {
        return super.name();
    }


}
