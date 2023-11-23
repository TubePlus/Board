package com.tubeplus.board_service.adapter.web.controller.vo.posting;

import com.tubeplus.board_service.application.posting.domain.posting.PostingFeedData;
import com.tubeplus.board_service.application.posting.port.in.PostingUseCase;
import com.tubeplus.board_service.application.posting.port.in.PostingUseCase.Feed;
import com.tubeplus.board_service.application.posting.domain.posting.PostingPageView;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;

import java.util.function.Predicate;

@RequiredArgsConstructor
public enum PostingsViewTypeReq {

    FEED {
        public boolean checkBadRequest(VoReadPostingSimpleData.Req reqParam) {
            return reqParam.getFeedSize() == null || reqParam.getFeedSize() <= 0;
        }

        public VoReadPostingSimpleData.Res driveService(PostingUseCase postingService,
                                                        VoReadPostingSimpleData.Req reqParam) {

            Feed<PostingFeedData> feedDataList
                    = postingService.feedPostingSimpleData(reqParam.newInfoToFeed());

            return VoReadPostingSimpleData.Res.of(feedDataList);
        }
    },

    PAGE {
        public boolean checkBadRequest(VoReadPostingSimpleData.Req reqParam) {

            return reqParam.getPageIndex() == null || reqParam.getPageIndex() < 0
                    || reqParam.getPageSize() == null || reqParam.getPageSize() <= 0;
        }

        public VoReadPostingSimpleData.Res driveService(PostingUseCase postingService,
                                                        VoReadPostingSimpleData.Req reqParam) {

            Page<PostingPageView> pagedPosting
                    = postingService.pagePostingSimpleData(reqParam.newInfoToPage());

            return VoReadPostingSimpleData.Res.of(pagedPosting);
        }
    };


    public abstract boolean checkBadRequest(VoReadPostingSimpleData.Req reqParam);

    public abstract VoReadPostingSimpleData.Res driveService(PostingUseCase postingService,
                                                             VoReadPostingSimpleData.Req reqParam);


    public static class PostingsViewTypeReqConverter implements Converter<String, PostingsViewTypeReq> {

        @Override
        public PostingsViewTypeReq convert(String name) {
            return valueOf(name);
        }

    }
}