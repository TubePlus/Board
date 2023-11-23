package com.tubeplus.board_service.adapter.web.controller.vo.posting;


import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;

import java.util.function.Predicate;


@AllArgsConstructor
public enum PostingsSearchTypeReq {

    ALL {
        public boolean checkBadRequest(VoReadPostingSimpleData.Req reqParam) {
            return false;
        }
    },
    BOARD_ID {
        public boolean checkBadRequest(VoReadPostingSimpleData.Req reqParam) {

            return reqParam.getBoardId() == null || reqParam.getBoardId() < 1;
        }
    },
    AUTHOR_UUID {
        public boolean checkBadRequest(VoReadPostingSimpleData.Req reqParam) {

            return reqParam.getAuthorUuid() == null || reqParam.getAuthorUuid().isBlank();
        }
    },
    TITLE_SEARCH {
        public boolean checkBadRequest(VoReadPostingSimpleData.Req reqParam) {

            return reqParam.getTitleContaining() == null || reqParam.getTitleContaining().isBlank();
        }
    },
    BY_DELETE_STATE {
        public boolean checkBadRequest(VoReadPostingSimpleData.Req reqParam) {

            return reqParam.getDeleted() == null;
        }
    };


    public abstract boolean checkBadRequest(VoReadPostingSimpleData.Req reqParam);


    public static class PostingsSearchTypeReqConverter implements Converter<String, PostingsSearchTypeReq> {

        @Override
        public PostingsSearchTypeReq convert(String name) {
            return valueOf(name);
        }

    }

}
