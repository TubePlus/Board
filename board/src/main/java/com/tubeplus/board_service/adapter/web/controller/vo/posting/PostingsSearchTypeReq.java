package com.tubeplus.board_service.adapter.web.controller.vo.posting;


import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;

import java.util.function.Predicate;


@AllArgsConstructor
public enum PostingsSearchTypeReq {

    ALL(reqParam -> false),
    BOARD_ID(reqParam
            -> reqParam.getBoardId() == null
            || reqParam.getBoardId() < 1),
    AUTHOR_UUID(reqParam
            -> reqParam.getAuthorUuid() == null
            || reqParam.getAuthorUuid().isBlank()),
    TITLE_SEARCH(reqParam
            -> reqParam.getTitleContaining() == null
            || reqParam.getTitleContaining().isBlank()),
    BY_DELETE_STATE(reqParam
            -> reqParam.getDeleted() == null);


    public final Predicate<VoReadPostingSimpleData.Req> checkBadRequest;


    public static class PostingsSearchTypeReqConverter implements Converter<String, PostingsSearchTypeReq> {

        @Override
        public PostingsSearchTypeReq convert(String name) {
            return valueOf(name);
        }

    }

}
