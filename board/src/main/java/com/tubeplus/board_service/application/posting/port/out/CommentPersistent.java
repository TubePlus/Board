package com.tubeplus.board_service.application.posting.port.out;


import com.tubeplus.board_service.application.posting.port.in.CommentUseCase;
import com.tubeplus.board_service.application.posting.port.in.CommentUseCase.PostCommentForm;
import lombok.Builder;
import lombok.Data;

public interface CommentPersistent {


    Long saveComment(SaveCommentDto dto);

    @Data
    @Builder
    class SaveCommentDto {
        private final Long postingId;
        private final Long parentId;
        private final String commenterUuid;
        private final String contents;
        public static SaveCommentDto builtFrom(PostCommentForm form) {
            return SaveCommentDto.builder()
                    .postingId(form.getPostingId())
                    .parentId(form.getParentId())
                    .commenterUuid(form.getCommenterUuid())
                    .contents(form.getContents())
                    .build();
        }
    }
}
