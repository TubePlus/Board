package com.tubeplus.board_service.external.web.driving_adapter.board;

import com.tubeplus.board_service.external.web.driving_adapter.board.vo.VoBoardProperty;
import com.tubeplus.board_service.domain.board.model.Board;
import com.tubeplus.board_service.domain.board.port.in.BoardUseCase;
import com.tubeplus.board_service.external.web.config.ApiResponse;
import com.tubeplus.board_service.external.web.config.ApiTag;
import com.tubeplus.board_service.external.web.driving_adapter.board.vo.PostPostingReqBody;
import com.tubeplus.board_service.external.web.driving_adapter.board.vo.BoardSearchType;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RequiredArgsConstructor

@ApiTag(path = "/api/v1/boards", name = "Board API", description = "게시판 CRUD API")
public class BoardController {

    private final BoardUseCase boardService;


    @Operation(summary = "게시판 생성", description = "게시판 생성, 생성된 게시판 id 반환")
    @PostMapping()
    public ApiResponse<Long> postBoard
            (
                    @Valid @RequestBody PostPostingReqBody reqBody
            ) {
        BoardUseCase.FormToMakeBoard form = reqBody.toFormToMakeBoard();
        log.info(form.toString());

        Board postedBoard = boardService.makeBoard(form);

        Long postedBoardId = postedBoard.getId();
        return ApiResponse.ofSuccess(postedBoardId);

    }


    @Operation(summary = "게시판 목록 조회", description = "community_id, status로 게시판 목록 조회")
    @GetMapping()
    public ApiResponse<List<Board>> getBoardList
            (
                    @RequestParam("community_id") Long communityId,
                    @RequestParam("board_search_type") BoardSearchType searchType,
                    @RequestParam(value = "board_name", required = false) String nameToSearch
            ) {
        log.info(communityId.toString());
        BoardUseCase.BoardsFindInfo findInfo
                = BoardUseCase.BoardsFindInfo.builder()
                .communityId(communityId)
                .visible(searchType.getVisible())
                .erase(searchType.getErase())
                .nameToSearch(nameToSearch)
                .build();
        log.info(findInfo.toString());


        List<Board> foundBoards = boardService.findCommuBoards(findInfo);


        return ApiResponse.ofSuccess(foundBoards);
    }


    @Operation(summary = "게시판 속성 조회", description = "특정 게시판 속성을 id로 조회")
    @GetMapping("/{boardId}")
    public ApiResponse<VoBoardProperty> getBoardProperty
            (
                    @PathVariable("boardId") Long boardId
            ) {

        Board board = boardService.findBoard(boardId);
        VoBoardProperty boardProperty = VoBoardProperty.builtFrom(board);

        return ApiResponse.ofSuccess(boardProperty);
    }
}

