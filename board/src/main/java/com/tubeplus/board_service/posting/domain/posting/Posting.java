package com.tubeplus.board_service.posting.domain.posting;


import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


@Data
@Slf4j
@Builder
public class Posting {

    private final long id;

    private final String authorUuid;

    private final long voteCount;

    private final long boardId;

    private final boolean pin;

    private final String contents;

    private final String title;

    private final boolean erase;

}
