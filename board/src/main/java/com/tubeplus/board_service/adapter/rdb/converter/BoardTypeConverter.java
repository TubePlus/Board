package com.tubeplus.board_service.adapter.rdb.converter;

import com.tubeplus.board_service.board.model.BoardType;
import jakarta.persistence.Converter;


@Converter(autoApply = true)
public class BoardTypeConverter extends AbstractBaseEnumConverter<BoardType, String, String> {
    public BoardTypeConverter() {
        super(BoardType.class);

    }
}
