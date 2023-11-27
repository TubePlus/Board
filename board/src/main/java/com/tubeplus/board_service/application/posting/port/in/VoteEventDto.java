package com.tubeplus.board_service.application.posting.port.in;

import lombok.Value;

@Value(staticConstructor = "of")
public class VoteEventDto {

    Long postingId;
    Integer voteDiff;

}
