package com.tubeplus.board_service.application.posting.port.out;


import com.tubeplus.board_service.global.Exceptionable;
import com.tubeplus.board_service.application.posting.domain.posting.Posting;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.Optional;

import static com.tubeplus.board_service.application.posting.port.in.PostingUseCase.*;

public interface PostingPersistent {

    Exceptionable<Optional<Posting>, Long> findPosting(long postingId);

    Exceptionable<Posting, UpdatePostingDto> updatePosting(UpdatePostingDto dto);

    @Getter
    @SuperBuilder
    abstract class UpdatePostingDto {
        protected long postingId;
    }

    @Data
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    class UpdatePinStateDto
            extends UpdatePostingDto {
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
            extends UpdatePostingDto {
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
            extends UpdatePostingDto {

        private final boolean softDelete;

        public static UpdateSoftDeleteDto builtFrom(ModifySoftDeleteInfo info) {

            return UpdateSoftDeleteDto.builder()
                    .postingId(info.getPostingId())
                    .softDelete(info.isSoftDelete())
                    .build();
        }

    }

}
