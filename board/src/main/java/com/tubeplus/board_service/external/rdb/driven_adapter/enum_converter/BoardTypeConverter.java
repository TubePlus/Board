package com.tubeplus.board_service.external.rdb.driven_adapter.enum_converter;

import com.tubeplus.board_service.domain.board.model.BoardType;

public class BoardTypeConverter extends AbstractBaseEnumConverter<BoardType, String, String> {
    public BoardTypeConverter() {
        super(BoardType.class);
    }
}
