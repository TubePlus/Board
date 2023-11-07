package com.tubeplus.board_service.application.posting.port.in;

import com.tubeplus.board_service.application.posting.domain.posting.Posting;
import com.tubeplus.board_service.application.posting.domain.posting.PostingView;
import lombok.Builder;
import lombok.Data;

import java.util.List;


public interface PostingUseCase {


    PostingView readPostingView(long postingId, String userUuid);


    Posting getPosting(long postingId);


    @Data
    @Builder
    class PostingSimpleInfo {
        private final Long id;
        private final String authorUuid;
        private final long voteCount;
        private final boolean pinned;
        private final String title;
        private final boolean withImage;
    }

    List<PostingSimpleInfo> readMyPostingTitles(String userUuid);

    List<PostingSimpleInfo> pagePostingTitles(Long boardId, PageDto dto);

    @Data
    @Builder
    class PageDto {
    }

    List<PostingSimpleInfo> feedPostingTitles(Long boardId, FeedDto dto);

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


    void modifyPostingPinState(ModifyPinStateInfo form);

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