package com.tubeplus.board_service.application.posting.port.in;

import com.tubeplus.board_service.application.posting.domain.posting.Posting;
import com.tubeplus.board_service.application.posting.domain.posting.PostingViewInfo;
import lombok.Builder;
import lombok.Data;

import java.util.List;


public interface PostingUseCase {


    PostingViewInfo readPosting(long postingId, String userUuid);




    @Data
    @Builder
    class PostingSimpleInfo {
        private final Long postingId;
        private final String title;
        private final boolean pin;
        private final boolean withImage;
        private final long voteCount;
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


    void pinPosting(long id);


    Posting modifyPosting(ModifyPostingForm form);//todo 작성자 권한체크 하기

    @Data
    @Builder
    class ModifyPostingForm {
        private final String userUuid;
        private final String title;
        private final String contents;
    }


    void softDeletePosting(long postingId);

}