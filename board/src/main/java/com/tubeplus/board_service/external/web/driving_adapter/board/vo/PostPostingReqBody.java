package com.tubeplus.board_service.external.web.driving_adapter.board.vo;

import com.tubeplus.board_service.domain.board.model.BoardType;
import com.tubeplus.board_service.domain.board.port.in.BoardUseCase;
import lombok.Value;

import java.time.LocalDateTime;

@Value
public class PostPostingReqBody {

    private long communityId;
    private String boardName;
    private BoardType boardType;
    private String boardDescription;
    private LocalDateTime limitDateTime;


    public BoardUseCase.FormToMakeBoard toFormToMakeBoard() {

        BoardUseCase.FormToMakeBoard makeRequest
                = BoardUseCase.FormToMakeBoard.builder().
                communityId(communityId).
                boardName(boardName).
                boardType(boardType).
                boardDescription(boardDescription).
                limitDateTime(limitDateTime).
                build();

        return makeRequest;
    }
}
