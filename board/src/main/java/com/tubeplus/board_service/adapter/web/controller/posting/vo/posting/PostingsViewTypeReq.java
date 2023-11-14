package com.tubeplus.board_service.adapter.web.controller.posting.vo.posting;

import com.tubeplus.board_service.adapter.web.controller.posting.vo.VoReadPostingSimpleData;
import com.tubeplus.board_service.application.posting.port.in.PostingUseCase;
import com.tubeplus.board_service.application.posting.port.in.PostingUseCase.Feed;
import com.tubeplus.board_service.application.posting.port.in.PostingUseCase.PostingSimpleData;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;

import java.util.function.Predicate;


@RequiredArgsConstructor
public enum PostingsViewTypeReq {

    FEED(reqParam -> reqParam.getFeedSize() == null) {
        public final VoReadPostingSimpleData.Res driveService(PostingUseCase postingService, VoReadPostingSimpleData.Req reqParam) {

            Feed<PostingSimpleData> postingDataFeed
                    = postingService.feedPostingSimpleData(reqParam.newInfoToFeed());

            return VoReadPostingSimpleData.Res.of(postingDataFeed);
        }
    },

    PAGE(reqParam -> reqParam.getPageIndex() == null || reqParam.getPageIndex() < 0
            || reqParam.getPageSize() <= 0) {
        public final VoReadPostingSimpleData.Res driveService(PostingUseCase postingService, VoReadPostingSimpleData.Req reqParam) {
            Page<PostingSimpleData> postingDataPage = postingService.pagePostingSimpleData(reqParam.newInfoToPage());

            return VoReadPostingSimpleData.Res.of(postingDataPage);
        }
    };


    public final Predicate<VoReadPostingSimpleData.Req> checkBadRequest;

    public abstract VoReadPostingSimpleData.Res driveService(PostingUseCase postingService, VoReadPostingSimpleData.Req reqParam);


    public static class PostingViewReqConverter implements Converter<String, PostingsViewTypeReq> {

        @Override
        public PostingsViewTypeReq convert(String name) {
            return valueOf(name);
        }

    }
}