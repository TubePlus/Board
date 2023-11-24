package com.tubeplus.board_service.adapter.rdb.board;

import com.tubeplus.board_service.adapter.rdb.common.AbstractBaseEnumConverter;
import com.tubeplus.board_service.application.board.domain.BoardType;
import jakarta.persistence.Converter;


@Converter(autoApply = true)
public class BoardTypeConverter extends AbstractBaseEnumConverter<BoardType, String, String> {
    public BoardTypeConverter() {
        super(BoardType.class);
    }
}
