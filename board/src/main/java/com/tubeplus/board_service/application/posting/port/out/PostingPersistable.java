package com.tubeplus.board_service.application.posting.port.out;


import com.tubeplus.board_service.application.posting.port.out.PostingPersistable.FindPostingsDto.SortedFindRange.SortBy.PivotField;
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

public interface PostingPersistable {

    Exceptionable<Optional<Posting>, Long> findPosting(long postingId);


    Exceptionable<Long, SavePostingDto> savePosting(SavePostingDto dto);

    @Data
    @Builder
    class SavePostingDto {
        private final Long boardId;
        private final String authorUuid;
        private final String title;
        private final String contents;
        public final boolean withImage;

        public static SavePostingDto builtFrom(MakePostingForm form) {
            return SavePostingDto.builder()
                    .boardId(form.getBoardId())
                    .authorUuid(form.getAuthorUuid())
                    .title(form.getTitle())
                    .contents(form.getContents())
                    .withImage(form.isWithImage())
                    .build();
        }
    }


    boolean existNextPosting(FindPostingsDto dto);

    Exceptionable<List<Posting>, FindPostingsDto> findPostings(FindPostingsDto dto);

    Exceptionable<Long, FindPostingsDto.FieldsFindCondition> countPostings(FindPostingsDto.FieldsFindCondition fieldsFindCondition);

    @Data(staticConstructor = "of")
    class FindPostingsDto {

        private final FieldsFindCondition fieldsFindCondition;
        private final SortedFindRange sortedRange;

        public static FindPostingsDto of(InfoToPagePostingData infoToPage) {
            return FindPostingsDto.of(
                    FieldsFindCondition.builtFrom(infoToPage),
                    SortedFindRange.of(infoToPage)
            );
        }

        public static FindPostingsDto of(InfoToFeedPostingData infoToFeed) {
            return FindPostingsDto.of(
                    FieldsFindCondition.builtFrom(infoToFeed),
                    SortedFindRange.of(infoToFeed)
            );
        }


        @Data
        @Builder
        public static class FieldsFindCondition {
            private Long cursorId;
            private final Long boardId;
            private final String authorUuid;
            private final Boolean pin;
            private final String titleContaining;
            private final String contentsContaining;
            private final Boolean softDelete;

            public static FieldsFindCondition builtFrom(InfoToPagePostingData infoToPage) {

                SearchPostingsInfo searchInfo = infoToPage.getSearchInfo();

                return FieldsFindCondition.builder()
                        .boardId(searchInfo.getBoardId())
                        .authorUuid(searchInfo.getAuthorUuid())
                        .pin(searchInfo.getPin())
                        .titleContaining(searchInfo.getTitleContaining())
                        .contentsContaining(searchInfo.getContentContaining())
                        .softDelete(searchInfo.getSoftDelete())
                        .build();
            }

            public static FieldsFindCondition builtFrom(InfoToFeedPostingData infoToFeed) {

                SearchPostingsInfo searchInfo = infoToFeed.getSearchInfo();

                return FieldsFindCondition.builder()
                        .cursorId(infoToFeed.getFeedReq().getCursorId())
                        .boardId(searchInfo.getBoardId())
                        .authorUuid(searchInfo.getAuthorUuid())
                        .pin(searchInfo.getPin())
                        .titleContaining(searchInfo.getTitleContaining())
                        .contentsContaining(searchInfo.getContentContaining())
                        .softDelete(searchInfo.getSoftDelete())
                        .build();
            }
        }


        @Data(staticConstructor = "of")
        public static class SortedFindRange {

            private final Integer limit;
            private final Long offset;
            private final SortBy sortBy;

            @Data(staticConstructor = "of")
            public static class SortBy {
                public final PivotField pivotField;
                private final boolean ascending;

                public enum PivotField {
                    ID,
                    VOTE_COUNT
                }
            }

            public static SortedFindRange of(InfoToPagePostingData pageInfo) {
                PageRequest pageReq = pageInfo.getPageReq();
                return SortedFindRange.of(
                        pageReq.getPageSize(),
                        pageReq.getOffset(),
                        SortBy.of(PivotField.ID, false)
                );
            }

            public static SortedFindRange of(InfoToFeedPostingData feedInfo) {

                Integer feedSize
                        = feedInfo.getFeedReq().getFeedSize();
                SortBy sortBy
                        = SortBy.of(PivotField.ID, false); //todo 프론트단에서 받아오는 정렬정보 여기 넣기

                return SortedFindRange.of(feedSize, null, sortBy);
            }

            public static SortedFindRange NotSpecified() {

                return SortedFindRange.of(0, 0L, null);
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
