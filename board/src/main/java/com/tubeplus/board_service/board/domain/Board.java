package com.tubeplus.board_service.board.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
public class Board {

    private Long id;
    private Long communityId;
    private String boardName;
    private BoardType boardType;
    private String boardDescription;
    private boolean visible;
    private LocalDateTime limitDateTime;
    private boolean erase;

}
