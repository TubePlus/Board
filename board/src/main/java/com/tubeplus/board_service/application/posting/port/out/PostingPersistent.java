package com.tubeplus.board_service.application.posting.port.out;


import com.tubeplus.board_service.global.Exceptionable;
import com.tubeplus.board_service.application.posting.domain.posting.Posting;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

import static com.tubeplus.board_service.application.posting.port.in.PostingUseCase.*;

public interface PostingPersistent {

    Exceptionable<Optional<Posting>, Long> findPosting(long postingId);


    @Data
    @Builder
    class FindPostingsCondition {
        private final Long boardId;
        private final String authorUuid;
        private final Boolean softDelete;

        public static FindPostingsCondition builtFrom(SearchPostingsInfo info) {
            return FindPostingsCondition.builder()
                    .boardId(info.getBoardId())
                    .authorUuid(info.getAuthorUuid())
                    .softDelete(info.getSoftDelete())
                    .build();
        }
    }

    Exceptionable<Page<Posting>, PagePostingsDto> pagePostings(PagePostingsDto dto);

    @Data
    @Builder
    class PagePostingsDto {
        private final FindPostingsCondition findCondition;
        private final PageRequest pageReq;

        public static PagePostingsDto builtFrom(InfoToPagePostingData info) {
            return PagePostingsDto.builder()
                    .findCondition(FindPostingsCondition.builtFrom
                            (info.getSearchInfo())
                    )
                    .pageReq(info.getPageReq())
                    .build();
        }
    }

    //todo 리턴값 수정
    Exceptionable<Page<Posting>, PagePostingsDto> CursorPostings(CursorPostingsDto dto);

    @Data
    @Builder
    class CursorPostingsDto {
        private final FindPostingsCondition findCondition;
        private final Long cursor;
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
