package com.tubeplus.board_service.adapter.web.controller.posting.vo.posting;


import com.tubeplus.board_service.adapter.web.controller.posting.vo.VoReadPostingSimpleData;
import lombok.AllArgsConstructor;

import java.util.function.Predicate;


@AllArgsConstructor
public enum PostingsSearchTypeReq {

    ALL_FOR_ADMIN(reqParam -> true),//todo admin 권한 조회관련 프론트와 상의
    BOARD_ID(reqParam
            -> reqParam.getBoardId() == null
            || reqParam.getBoardId() < 1),
    AUTHOR_UUID(reqParam
            -> reqParam.getAuthorUuid() == null
            || reqParam.getAuthorUuid().isBlank()),
    TITLE_SEARCH(reqParam
            -> reqParam.getTitleContaining() == null
            || reqParam.getTitleContaining().isBlank()),
    BY_DELETE_STATE(reqParam -> reqParam.getDeleted() == null);


    public final Predicate<VoReadPostingSimpleData.Req> checkBadRequest;


    public static class Converter implements org.springframework.core.convert.converter.Converter<String, PostingsSearchTypeReq> {

        @Override
        public PostingsSearchTypeReq convert(String name) {
            return valueOf(name);
        }

    }

}
