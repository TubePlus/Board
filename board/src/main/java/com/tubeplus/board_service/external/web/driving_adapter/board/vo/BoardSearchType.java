package com.tubeplus.board_service.external.web.driving_adapter.board.vo;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.core.convert.converter.Converter;


@AllArgsConstructor
@Getter
public enum BoardSearchType {

    ACCESSIBLE(true, false),
    INACCESSIBLE(false, false),

    //creator, manager, admin
    ERASED(null, true),

    //admin
    ALL(null, null);


    private final Boolean visible;
    private final Boolean erase;

    public static class BoardSearchTypeConverter implements Converter<String, BoardSearchType> {

        @Override
        public BoardSearchType convert(String name) {
            return valueOf(name);
        }

    }
}