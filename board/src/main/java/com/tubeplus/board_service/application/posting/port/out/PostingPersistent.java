package com.tubeplus.board_service.application.posting.port.out;


import com.querydsl.core.types.OrderSpecifier;
import com.tubeplus.board_service.global.Exceptionable;
import com.tubeplus.board_service.application.posting.domain.posting.Posting;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static com.tubeplus.board_service.application.posting.port.in.PostingUseCase.*;

public interface PostingPersistent {

    Exceptionable<Optional<Posting>, Long> findPosting(long postingId);


    Exceptionable<List<Posting>, FindPostingsDto> findPostings(FindPostingsDto dto);

    Exceptionable<Long, FindPostingsDto.ConditionByFields> countPostings(FindPostingsDto.ConditionByFields conditionByFields);

    @Data(staticConstructor = "of")
    class FindPostingsDto {

        private final ConditionByFields conditionByFields;
        private final SortScope sortScope;


        public static FindPostingsDto of(InfoToPagePostingData infoToPage) {
            return FindPostingsDto.of
                    (
                            ConditionByFields.builtFrom(infoToPage),
                            SortScope.of(infoToPage)
                    );
        }

        public static FindPostingsDto of(InfoToFeedPostingData infoToFeed) {
            return FindPostingsDto.of
                    (
                            ConditionByFields.builtFrom(infoToFeed),
                            SortScope.of(infoToFeed)
                    );
        }


        @Data
        @Builder
        public static class ConditionByFields {
            private final Long cursorId;
            private final Long boardId;
            private final String authorUuid;
            private final Boolean pin;
            private final String titleContaining;
            private final String contentsContaining;
            private final Boolean softDelete;

            public static ConditionByFields builtFrom(InfoToPagePostingData infoToPage) {

                SearchPostingsInfo searchInfo = infoToPage.getSearchInfo();

                return ConditionByFields.builder()
                        .boardId(searchInfo.getBoardId())
                        .authorUuid(searchInfo.getAuthorUuid())
                        .pin(searchInfo.getPin())
                        .titleContaining(searchInfo.getTitleContaining())
                        .contentsContaining(searchInfo.getContentsContaining())
                        .softDelete(searchInfo.getSoftDelete())
                        .build();
            }

            public static ConditionByFields builtFrom(InfoToFeedPostingData infoToFeed) {

                SearchPostingsInfo searchInfo = infoToFeed.getSearchInfo();

                return ConditionByFields.builder()
                        .cursorId(infoToFeed.getFeedReq().getCursorId())
                        .boardId(searchInfo.getBoardId())
                        .authorUuid(searchInfo.getAuthorUuid())
                        .pin(searchInfo.getPin())
                        .titleContaining(searchInfo.getTitleContaining())
                        .contentsContaining(searchInfo.getContentsContaining())
                        .softDelete(searchInfo.getSoftDelete())
                        .build();
            }
        }


        @Data(staticConstructor = "of")
        public static class SortScope {
            private final Integer limit;
            private final Long offset;
            private final OrderSpecifier orderSpec;

            public static SortScope of(InfoToPagePostingData pageInfo) {
                PageRequest pageReq = pageInfo.getPageReq();
                return SortScope.of(pageReq.getPageSize(), pageReq.getOffset(), null);
            }

            public static SortScope of(InfoToFeedPostingData feedInfo) {
                FeedRequest feedReq = feedInfo.getFeedReq();
                return SortScope.of(feedReq.getFeedSize(), null, null);
            }

            public static SortScope NotSpecified() {

                return SortScope.of(0, 0L, null);
            }
        }
    }


    Exceptionable<Posting, BaseUpdatePostingDto> updatePosting(BaseUpdatePostingDto dto);

    @Getter
    @SuperBuilder
    abstract class BaseUpdatePostingDto {
        protected long postingId;
    }

    @Data
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    class UpdatePinStateDto
            extends BaseUpdatePostingDto {
        private final boolean pin;

        public static UpdatePinStateDto builtFrom(ModifyPinStateInfo form) {
            return UpdatePinStateDto.builder()
                    .postingId(form.getPostingId())
                    .pin(form.isPinned())
                    .build();
        }
    }

    @Data
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    class UpdateArticleDto
            extends BaseUpdatePostingDto {
        private final String title;
        private final String contents;

        public static UpdateArticleDto builtFrom(long postingId,
                                                 ModifyArticleForm form) {
            return UpdateArticleDto.builder()
                    .postingId(postingId)
                    .title(form.getTitle())
                    .contents(form.getContents())
                    .build();
        }

    }

    @Data
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    class UpdateSoftDeleteDto
            extends BaseUpdatePostingDto {

        private final boolean softDelete;

        public static UpdateSoftDeleteDto builtFrom(ModifySoftDeleteInfo info) {

            return UpdateSoftDeleteDto.builder()
                    .postingId(info.getPostingId())
                    .softDelete(info.isSoftDelete())
                    .build();
        }

    }

}
