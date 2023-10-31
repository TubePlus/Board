package com.tubeplus.board_service.adapter.web.controller.posting.vo;

import com.tubeplus.board_service.posting.port.in.PostingUseCase;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;


@Data
@Builder
public class PostingTitleVo implements Serializable {
    private final long postingId;
    private final boolean pin;
    private final String title;


    public static PostingTitleVo builtFrom(PostingUseCase.PostingTitle title) {

        return PostingTitleVo.builder()
                .postingId(title.getPostingId())
                .pin(title.isPin())
                .title(title.getTitle())
                .build();
    }
}
