package com.tubeplus.board_service.adapter.web.controller.posting.vo.posting;


import jakarta.validation.constraints.NotEmpty;
import org.springframework.core.convert.converter.Converter;

public enum PostingsSearchReqType {

    ALL,
    BOARD_ID,
    AUTHOR_UUID,
    TITLE,
    ERASED;


    public static class ReqConverter implements Converter<String, PostingsSearchReqType> {

        @Override
        public PostingsSearchReqType convert(String name) {
            return valueOf(name);
        }

    }


}
