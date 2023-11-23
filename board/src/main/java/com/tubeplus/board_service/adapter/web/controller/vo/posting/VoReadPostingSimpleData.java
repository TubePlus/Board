package com.tubeplus.board_service.adapter.web.controller.vo.posting;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.tubeplus.board_service.application.posting.domain.posting.PostingFeedData;
import com.tubeplus.board_service.application.posting.port.in.PostingUseCase.Feed;
import com.tubeplus.board_service.application.posting.port.in.PostingUseCase.FeedRequest;
import com.tubeplus.board_service.application.posting.port.in.PostingUseCase.InfoToFeedPostingData;
import com.tubeplus.board_service.application.posting.port.in.PostingUseCase.InfoToPagePostingData;
import com.tubeplus.board_service.application.posting.domain.posting.PostingPageView;
import com.tubeplus.board_service.application.posting.port.in.PostingUseCase.SearchPostingsInfo;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


@Value
@Slf4j
@JsonDeserialize
public class VoReadPostingSimpleData {

    @Value
    @Slf4j
    public static class Req { //todo 챗지피티가 스프링이 알아서 케밥->카멜로 역직렬화 해준다고 함, 확인요망
        // 필드 검색 조건
        // 주요 검색 조건 - searchReqType에 따라 필수 요청이 될 수 있는 파라미터
        private final Long boardId;
        private final String authorUuid;
        private final String titleContaining;
        private final String contentsContaining;
        // 부가 조건 - 항상 필수 요청이 아닌 파라미터
        private final Boolean pin;
        private final Boolean deleted;

        // 요청된 화면표시 타입 관련
        // pagination 요청시
        private final Integer pageIndex;
        private final Integer pageSize;
        // feed(무한스크롤) 요청시
        private final Long cursorId;
        private final Integer feedSize;

        // Admin 요청시 권한 조회 관련
        private final String adminUuid;


        public InfoToFeedPostingData newInfoToFeed() {

            return InfoToFeedPostingData.of(
                    buildSearchInfo(), newFeedRequest()
            );
        }

        public InfoToPagePostingData newInfoToPage() {
            return InfoToPagePostingData.of(
                    buildSearchInfo(), newPageRequest()
            );
        }

        public SearchPostingsInfo buildSearchInfo() {

            return SearchPostingsInfo.builder()
                    .boardId(boardId)
                    .authorUuid(authorUuid)
                    .titleContaining(titleContaining)
                    .contentContaining(contentsContaining)
                    .pin(pin)
                    .softDelete(deleted)
                    .build();
        }

        public PageRequest newPageRequest() {
            return PageRequest.of(pageIndex, pageSize);
        }

        public FeedRequest newFeedRequest() {
            return FeedRequest.of(cursorId, feedSize);
        }


    }


    @Value
    @Slf4j
    public static class Res {

        private final Page<PostingPageView> pagedPostingData;
        private final Feed<PostingFeedData> fedPostingData;

        public static Res of(Page<PostingPageView> pagedPostingData) {

            return new Res(pagedPostingData, null);
        }

        public static Res of(Feed<PostingFeedData> fedPostingData) {

            return new Res(null, fedPostingData);
        }
    }
}
