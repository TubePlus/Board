package com.tubeplus.board_service.adapter.web.controller.posting.vo;


import com.tubeplus.board_service.application.posting.port.in.PostingUseCase.Feed;
import com.tubeplus.board_service.application.posting.port.in.PostingUseCase.PostingSimpleData;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;


@Value
@Slf4j
public class VoReadPostingSimpleData {

    @Value
    @Slf4j
    public static class Res {
        private final Page<PostingSimpleData> pagedPostingData;
        private final Feed<PostingSimpleData> fedPostingData;

        public static Res of(Page<PostingSimpleData> pagedPostingData) {

            return new Res(pagedPostingData, null);
        }

        public static Res of (Feed<PostingSimpleData> fedPostingData) {

            return new Res(null, fedPostingData);
        }
    }
}
