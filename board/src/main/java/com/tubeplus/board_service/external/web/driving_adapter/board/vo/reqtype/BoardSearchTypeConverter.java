package com.tubeplus.board_service.external.web.driving_adapter.board.vo.reqtype;

import org.springframework.core.convert.converter.Converter;


public class BoardSearchTypeConverter implements Converter<String, BoardSearchType> {

    @Override
    public BoardSearchType convert(String name) {
        return BoardSearchType.valueOf(name);
    }

}
