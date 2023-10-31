package com.tubeplus.board_service.posting.port.in;

import com.tubeplus.board_service.posting.domain.Posting;
import com.tubeplus.board_service.posting.domain.PostingView;
import lombok.Builder;
import lombok.Data;

import java.util.List;

public interface PostingUseCase {


    PostingView viewPosting(long id);


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

}
