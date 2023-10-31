package com.tubeplus.board_service.adapter.web.controller.posting.vo;

import org.springframework.core.convert.converter.Converter;

public enum PostingPageType {

    FEED,
    LIST;


    public static class ReqConverter implements Converter<String, PostingPageType> {

        @Override
        public PostingPageType convert(String name) {
            return valueOf(name);
        }

    }
}