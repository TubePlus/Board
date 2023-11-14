package com.tubeplus.board_service.application.posting.port.in;

import com.tubeplus.board_service.adapter.web.controller.posting.vo.VoReadPostingSimpleData;
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
    class PostingSimpleData {
        private final Long id;
        private final String authorUuid;
        private final long voteCount;
        private final boolean pinned;
        private final String title;
        private final boolean withImage;

        public static PostingSimpleData builtFrom(Posting posting) {
            return PostingSimpleData.builder()
                    .id(posting.getId())
                    .authorUuid(posting.getAuthorUuid())
                    .voteCount(posting.getVoteCount())
                    .pinned(posting.isPin())
                    .title(posting.getTitle())
                    .withImage(posting.isWithImage())
                    .build();
        }
    }

    @Data
    @Builder
    class SearchPostingsInfo {
        private final Long boardId;
        private final String authorUuid;
        private final String titleContaining;
        private final String contentsContaining;
        private final Boolean pin;
        private final Boolean softDelete;

    }

    Page<PostingSimpleData> pagePostingSimpleData(InfoToPagePostingData info);

    @Data(staticConstructor = "of")
    class InfoToPagePostingData {
        private final SearchPostingsInfo searchInfo;
        private final PageRequest pageReq;
    }


    Feed<PostingSimpleData> feedPostingSimpleData(InfoToFeedPostingData info);

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
    }


    void modifyPostingPinState(ModifyPinStateInfo info);

    @Data
    @Builder
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
    }


    void modifyDeletePosting(ModifySoftDeleteInfo info);

    @Data(staticConstructor = "of")
    class ModifySoftDeleteInfo {
        private final long postingId;
        private final boolean softDelete;
    }

}