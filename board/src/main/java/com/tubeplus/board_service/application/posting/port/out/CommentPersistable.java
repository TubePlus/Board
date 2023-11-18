package com.tubeplus.board_service.application.posting.port.out;


import com.tubeplus.board_service.application.posting.domain.comment.Comment;
import com.tubeplus.board_service.application.posting.port.in.WebCommentUseCase.PostCommentForm;
import com.tubeplus.board_service.application.posting.port.in.WebCommentUseCase.ReadCommentsInfo;
import com.tubeplus.board_service.global.Exceptionable;
import lombok.Builder;
import lombok.Data;

import java.util.List;

public interface CommentPersistable {


    Exceptionable<Boolean, Long> deleteComment(Long idToDelete);

    Exceptionable<Comment, SaveCommentDto> saveComment(SaveCommentDto dto);

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


    Exceptionable<List<Comment>, FindCommentDto> findComments(FindCommentDto dto);

    @Data(staticConstructor = "of")
    class FindCommentDto {
        private final long postingId;
        private final Long parentId;

        public static FindCommentDto of(ReadCommentsInfo readInfo) {
            return FindCommentDto.of(
                    readInfo.getPostingId(),
                    readInfo.getParentCommentId()
            );
        }

        public boolean isFindingParent() {
            return parentId == null;
        }

        public boolean isFindingChildren() {
            return parentId != null;
        }

    }


    Exceptionable<Comment, UpdateCommentDto> updateComment(UpdateCommentDto dto);

    @Data(staticConstructor = "of")
    class UpdateCommentDto {
        private final Long idToModify;
        private final String content;
    }
}
