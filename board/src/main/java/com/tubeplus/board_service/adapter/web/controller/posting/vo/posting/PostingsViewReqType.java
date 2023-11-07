package com.tubeplus.board_service.adapter.web.controller.posting.vo.posting;

import org.springframework.core.convert.converter.Converter;


public enum PostingsViewReqType {

    FEED,
    PAGE;


    public static class ReqConverter implements Converter<String, PostingsViewReqType> {

        @Override
        public PostingsViewReqType convert(String name) {
            return valueOf(name);
        }

    }
}