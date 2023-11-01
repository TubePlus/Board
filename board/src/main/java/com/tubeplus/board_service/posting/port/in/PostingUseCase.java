package com.tubeplus.board_service.posting.port.in;

import com.tubeplus.board_service.posting.domain.comment.CommentViewInfo;
import com.tubeplus.board_service.posting.domain.posting.Posting;
import com.tubeplus.board_service.posting.domain.posting.PostingViewInfo;
import com.tubeplus.board_service.posting.domain.vote.PostingVote;
import lombok.Builder;
import lombok.Data;

import java.util.List;

public interface PostingUseCase {


    PostingViewInfo viewPosting(long id);


    void deleteComment(long idToDelete);


    @Data
    @Builder
    class PostingTitle {
        private final Long postingId;
        private final String title;
        private final boolean pin;
    }

    List<PostingTitle> pagePostingTitles(Long boardId, PageDto dto);

    @Data
    @Builder
    class PageDto {
    }

    List<PostingTitle> feedPostingTitles(Long boardId, FeedDto dto);

    @Data
    @Builder
    class FeedDto {
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


    void pinPosting(long id);


    Posting modifyPosting(ModifyPostingForm form);//todo 작성자 권한체크 하기

    @Data
    @Builder
    class ModifyPostingForm {
        private final String userUuid;
        private final String title;
        private final String contents;
    }


    void softDeletePosting(long id);


    //vote
    long votePosting(PostingVote vote);


    long modifyPostingVote(PostingVote vote);


    long cancelVote();


    //comment
    long writeComment(PostCommentForm form);

    @Data
    @Builder
    class PostCommentForm {
        private final Long postingId;
        private final Long parentId;
        private final String commenterUuid;
        private final String contents;
    }


    CommentViewInfo readComment(ReadCommentDto dto);

    @Data
    @Builder
    public static class ReadCommentDto {
        private final long postingId;
        private final Long parentId;
    }


    CommentViewInfo modifyComment(Long idToModify, String contents);
}
