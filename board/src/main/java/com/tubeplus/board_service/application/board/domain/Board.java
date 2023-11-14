package com.tubeplus.board_service.application.board.domain;

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
    private boolean softDelete;

    private LocalDateTime limitDateTime;
}
