package com.tubeplus.board_service.application.posting.port.in;

import com.tubeplus.board_service.application.posting.domain.posting.PostingFeedData;
import com.tubeplus.board_service.application.posting.domain.posting.PostingPageView;
import com.tubeplus.board_service.application.posting.domain.posting.Posting;
import com.tubeplus.board_service.application.posting.domain.posting.PostingView;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;


public interface PostingUseCase {

    PostingView readPostingView(long postingId, String userUuid);


    @Data
    @Builder
    class SearchPostingsInfo {
        private final Long boardId;
        private final String authorUuid;
        private final String titleContaining;
        private final String contentContaining;
        private final Boolean pin;
        private final Boolean softDelete;
    }

    Page<PostingPageView> pagePostingSimpleData(InfoToPagePostingData info);

    @Data(staticConstructor = "of")
    class InfoToPagePostingData {
        private final SearchPostingsInfo searchInfo;
        private final PageRequest pageReq;
    }

    Feed<PostingFeedData> feedPostingSimpleData(InfoToFeedPostingData info);

    @Data(staticConstructor = "of")
    class Feed<T> {
        private final List<T> data;
        private final Long lastCursoredId;
        private final boolean hasNextFeed;

        public final <U> Feed<U> map(Function<T, U> mapper) {

            List<U> mappedData = data.stream().map(mapper).collect(toList());

            return Feed.of(mappedData, lastCursoredId, hasNextFeed);
        }
    }

    @Data(staticConstructor = "of")
    class InfoToFeedPostingData {
        private final SearchPostingsInfo searchInfo;
        private final FeedRequest feedReq;
    }

    @Data(staticConstructor = "of")
    class FeedRequest {
        private final Long cursorId;
        private final Integer feedSize;
    }


    Long makePosting(MakePostingForm form);

    @Data
    @Builder
    class MakePostingForm {
        private final Long boardId;
        private final String authorUuid;
        private final String title;
        private final String contents;
        private final boolean withImage;
    }


    void modifyPostingPinState(ModifyPinStateInfo info);

    @Data(staticConstructor = "of")
    class ModifyPinStateInfo {
        private final long postingId;
        private final boolean pinned;
    }


    Posting modifyPostingArticle(long postingId, ModifyArticleForm form);

    @Data
    @Builder
    class ModifyArticleForm {
        private final String userUuid;
        private final String title;
        private final String contents;
        private final boolean withImage;
    }


    void modifyPostingDelete(ModifySoftDeleteInfo info);

    @Data(staticConstructor = "of")
    class ModifySoftDeleteInfo {
        private final long postingId;
        private final boolean softDelete;
    }

}