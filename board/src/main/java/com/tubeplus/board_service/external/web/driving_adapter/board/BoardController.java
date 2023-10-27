package com.tubeplus.board_service.external.web.driving_adapter.board;

import com.tubeplus.board_service.external.web.driving_adapter.board.vo.BoardPropertyVo;
import com.tubeplus.board_service.board.model.Board;
import com.tubeplus.board_service.board.port.in.BoardUseCase;
import com.tubeplus.board_service.external.web.config.ApiResponse;
import com.tubeplus.board_service.external.web.config.ApiTag;
import com.tubeplus.board_service.external.web.driving_adapter.board.vo.PostPostingReqBody;
import com.tubeplus.board_service.external.web.driving_adapter.board.vo.BoardSearchType;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
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
        BoardUseCase.FormToMakeBoard form = reqBody.buildFormToMakeBoard();
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

        BoardUseCase.BoardListInfo findInfo
                = BoardUseCase.BoardListInfo.builder()
                .communityId(communityId)
                .visible(searchType.getVisible())
                .erase(searchType.getErase())
                .nameToSearch(nameToSearch)
                .build();
        log.info(findInfo.toString());


        List<Board> foundBoards = boardService.listCommuBoards(findInfo);


        return ApiResponse.ofSuccess(foundBoards);
    }


    @Operation(summary = "게시판 속성 조회", description = "특정 게시판 속성을 id로 조회")
    @GetMapping("/{boardId}")
    public ApiResponse<BoardPropertyVo> getBoardProperty
            (
                    @PathVariable("boardId") Long boardId
            ) {
        log.info(boardId.toString());

        Board foundBoard
                = boardService.findBoard(boardId);

        BoardPropertyVo boardProperty
                = BoardPropertyVo.builtFrom(foundBoard);

        return ApiResponse.ofSuccess(boardProperty);
    }


    @Operation(summary = "게시판 속성 변경", description = "특정 게시판 속성을 변경")
    @PutMapping("/{boardId}")
    public ApiResponse updateBoardProperty
            (
                    @PathVariable("boardId") Long boardId,
                    @RequestBody BoardPropertyVo updateReqBody
            ) {
        log.info(boardId.toString());
//        if (haveNoUpdate(updateReqBody)) {//todo 디버깅
//            throw new BusinessException(ErrorCode.BAD_REQUEST);
//        }

        BoardUseCase.BoardProperty toUpdate
                = updateReqBody.buildBoardProperty();

        boardService.updateBoardProperty(boardId, toUpdate);


        return ApiResponse.ofSuccess(null);
    }

    //
    private boolean haveNoUpdate(BoardPropertyVo updateReq) {

        //한개의 field라도 들어온게 있다면 update가 있는걸로 간주, false 리턴
        for (Field updateField : updateReq.getClass().getFields()) {
            Object fieldInstance = null;
            try {
                fieldInstance = updateField.get(updateReq);
            } catch (NullPointerException e) {
                log.error("bi");
            } catch (Exception e) {
                log.error("hi");
            }

            if (fieldInstance != null) {
                log.error("asdlkfjasdlk");
                return false;
            }

        }

        return true;
    }


    @Operation(summary = "게시판 삭제", description = "특정 id의 게시판을 soft delete 처리")
    @DeleteMapping("/{boardId}")
    public ApiResponse deleteBoard
            (
                    @Valid @PathVariable("boardId")
                    @NotNull @Min(1) Long boardId
            ) {

        boardService.softlyDeleteBoard(boardId);

        return ApiResponse.ofSuccess(null);
    }

}


