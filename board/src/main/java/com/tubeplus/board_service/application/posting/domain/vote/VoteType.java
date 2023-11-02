package com.tubeplus.board_service.application.posting.domain.vote;

import com.tubeplus.board_service.global.base.BaseEnum;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum VoteType implements BaseEnum<Character, String> {


    LIKE('+'),
    HATE('-');


    private final char code;


    @Override
    public Character getCode() {
        return this.code;
    }

    @Override
    public String getValue() {
        return name();
    }
}
