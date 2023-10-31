package com.tubeplus.board_service.posting.domain;

import com.tubeplus.board_service.global.base.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
public enum PostingVoteType implements BaseEnum<Character, String> {

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
