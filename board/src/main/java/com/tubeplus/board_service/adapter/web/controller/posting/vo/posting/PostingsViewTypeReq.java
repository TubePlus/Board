package com.tubeplus.board_service.adapter.web.controller.posting.vo.posting;

import com.tubeplus.board_service.adapter.web.controller.posting.vo.VoReadPostingSimpleData;
import com.tubeplus.board_service.application.posting.port.in.PostingUseCase;
import com.tubeplus.board_service.application.posting.port.in.PostingUseCase.Feed;
import com.tubeplus.board_service.application.posting.port.in.PostingUseCase.PostingSimpleData;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;


public enum PostingsViewTypeReq {

    FEED {
        public final VoReadPostingSimpleData.Res driveService(PostingUseCase postingService,
                                                        VoReadPostingSimpleData.Req reqParam) {
            Feed<PostingSimpleData> postingDataFeed
                    = postingService.feedPostingSimpleData(reqParam.newInfoToFeed());

            return VoReadPostingSimpleData.Res.of(postingDataFeed);
        }
    },
    PAGE {
        public final VoReadPostingSimpleData.Res driveService(PostingUseCase postingService,
                                                        VoReadPostingSimpleData.Req reqParam) {
            Page<PostingSimpleData> postingDataPage
                    = postingService.pagePostingSimpleData(reqParam.newInfoToPage());

            return VoReadPostingSimpleData.Res.of(postingDataPage);
        }
    };


    public abstract VoReadPostingSimpleData.Res driveService(PostingUseCase postingService,
                                                                VoReadPostingSimpleData.Req reqParam);


    public static class ReqConverter implements Converter<String, PostingsViewTypeReq> {

        @Override
        public PostingsViewTypeReq convert(String name) {
            return valueOf(name);
        }

    }
}