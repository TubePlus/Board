package com.tubeplus.board_service.external.web.driving_adapter.board;

import com.tubeplus.board_service.domain.board.model.Board;
import com.tubeplus.board_service.domain.board.port.in.BoardUseCase;
import com.tubeplus.board_service.external.web.driving_adapter.ApiResponse;
import com.tubeplus.board_service.external.web.driving_adapter.ApiTag;
import com.tubeplus.board_service.external.web.driving_adapter.board.vo.PostPostingReqBody;
import com.tubeplus.board_service.external.web.driving_adapter.board.vo.reqtype.BoardSearchType;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Slf4j
@RequiredArgsConstructor

@ApiTag(path = "/api/v1/boards", name = "Board API", description = "게시판 CRUD API")
public class BoardController {

    private final BoardUseCase boardService;


    @Operation(summary = "게시판 생성", description = "게시판 생성, 생성된 게시판 id 반환")
    @PostMapping()
    public ApiResponse<Long> postPosting
            (
                    @Valid @RequestBody PostPostingReqBody reqBody
            ) {

        BoardUseCase.FormToMakeBoard form = reqBody.toFormToMakeBoard();

        Board postedBoard = boardService.makeBoard(form);

        Long postedBoardId = postedBoard.getId();
        return ApiResponse.ofSuccess(postedBoardId);

    }


    @Operation(summary = "게시판 목록 조회", description = "community_id, status로 게시판 목록 조회")
    @GetMapping()
    public ApiResponse<List<Board>> getBoards
            (
                    @RequestParam("community_id") Long communityId,
                    @RequestParam("board_search_type") BoardSearchType searchType,
                    @RequestParam(value = "board_name", required = false) String nameToSearch
            ) {

        BoardUseCase.BoardsFindInfo searchInfo
                = BoardUseCase.BoardsFindInfo.builder()
                .communityId(communityId)
                .visible(searchType.getVisible())
                .erase(searchType.getErase())
                .nameToSearch(nameToSearch)
                .build();

        List<Board> foundBoards = boardService.findCommuBoards(searchInfo);


        return ApiResponse.ofSuccess(foundBoards);
    }

}

