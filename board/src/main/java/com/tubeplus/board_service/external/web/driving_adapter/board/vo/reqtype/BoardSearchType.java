package com.tubeplus.board_service.external.web.driving_adapter.board.vo.reqtype;


import com.tubeplus.board_service.domain.board.port.in.BoardUseCase;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum BoardSearchType {

    ACCESSIBLE(true, false),
    INACCESSIBLE(false, false),

    //creator, manager, admin
    ERASED(null, true),

    //admin
    ALL(null, null);


    private final Boolean visible;
    private final Boolean erase;

}