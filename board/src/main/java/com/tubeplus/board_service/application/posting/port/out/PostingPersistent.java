package com.tubeplus.board_service.application.posting.port.out;


import com.tubeplus.board_service.global.Exceptionable;
import com.tubeplus.board_service.application.posting.domain.posting.Posting;
import lombok.Builder;
import lombok.Data;

import java.util.Optional;

import static com.tubeplus.board_service.application.posting.port.in.PostingUseCase.*;

public interface PostingPersistent {

    Exceptionable
            <Optional<Posting>, Long> findPosting(long postingId);

    Exceptionable
            <Boolean, Long> softDeletePosting(long postingId);

    Exceptionable
            <Boolean, Long> changePinState(long id);

    Exceptionable
            <Posting, UpdateWritingDto> updatePostingWriting(UpdateWritingDto dto);

    @Data
    @Builder
    class UpdateWritingDto {
        private final long postingId;
        private final String userUuid;
        private final String title;
        private final String contents;

        public static UpdateWritingDto builtFrom(long postingId,
                                                 ModifyPostingForm form) {
            return UpdateWritingDto
                    .builder()
                    .postingId(postingId)
                    .userUuid(form.getUserUuid())
                    .title(form.getTitle())
                    .contents(form.getContents())
                    .build();
        }

    }
}
