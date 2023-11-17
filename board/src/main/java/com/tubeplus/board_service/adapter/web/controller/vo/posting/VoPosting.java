package com.tubeplus.board_service.adapter.web.controller.vo.posting;

import com.tubeplus.board_service.application.posting.domain.posting.Posting;
import lombok.Builder;
import lombok.Value;


@Value
@Builder
public class VoPosting {

    private final Long id;
    private final String authorUuid;
    private final long voteCounts;
    private final long boardId;
    private final boolean pin;
    private final String contents;
    private final String title;
    private final boolean softDelete;


    public static VoPosting builtFrom(Posting posting) {

        return VoPosting.builder()
                .id(posting.getId())
                .authorUuid(posting.getAuthorUuid())
                .voteCounts(posting.getVoteCount())
                .boardId(posting.getBoardId())
                .pin(posting.isPin())
                .contents(posting.getContents())
                .title(posting.getTitle())
                .softDelete(posting.isSoftDelete())
                .build();
    }

}
