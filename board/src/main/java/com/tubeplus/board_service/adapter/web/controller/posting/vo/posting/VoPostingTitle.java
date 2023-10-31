package com.tubeplus.board_service.adapter.web.controller.posting.vo.posting;

import com.tubeplus.board_service.posting.port.in.PostingUseCase;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;


@Data
@Builder
public class VoPostingTitle implements Serializable {
    private final long postingId;
    private final boolean pin;
    private final String title;


    public static VoPostingTitle builtFrom(PostingUseCase.PostingTitle title) {

        return VoPostingTitle.builder()
                .postingId(title.getPostingId())
                .pin(title.isPin())
                .title(title.getTitle())
                .build();
    }
}
