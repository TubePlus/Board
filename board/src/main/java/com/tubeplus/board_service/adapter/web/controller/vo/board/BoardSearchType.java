package com.tubeplus.board_service.adapter.web.controller.vo.board;


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

    public static class ReqConverter implements Converter<String, BoardSearchType> {

        @Override
        public BoardSearchType convert(String name) {
            return valueOf(name);
        }

    }
}