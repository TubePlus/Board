package com.tubeplus.board_service.adapter.web.controller.posting.vo.posting;


import com.tubeplus.board_service.adapter.web.controller.posting.vo.VoReadPostingSimpleData;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;

import java.util.function.Predicate;


@AllArgsConstructor
public enum PostingsSearchTypeReq {

    ALL(reqParam -> true),//todo admin 권한 조회관련 프론트와 상의
    BOARD_ID(reqParam -> reqParam.getBoardId() != null),
    AUTHOR_UUID(reqParam -> reqParam.getAuthorUuid() != null),
    TITLE(reqParam -> reqParam.getTitleContaining() != null),
    DELETED(reqParam -> reqParam.getDeleted() != null);


    private final Predicate<VoReadPostingSimpleData.Req> isBadRequest;

    public final boolean notPossibleWith(VoReadPostingSimpleData.Req reqParam) {
        return isBadRequest.test(reqParam);
    }

    public static class ReqConverter implements Converter<String, PostingsSearchTypeReq> {

        @Override
        public PostingsSearchTypeReq convert(String name) {
            return valueOf(name);
        }

    }

}
