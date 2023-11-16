package com.tubeplus.board_service.adapter.web.controller.board;

import com.tubeplus.board_service.adapter.web.common.ApiTag;
import com.tubeplus.board_service.adapter.web.controller.board.vo.ReqUpdateTimeLimitPropertyBody;
import com.tubeplus.board_service.adapter.web.error.BusinessException;
import com.tubeplus.board_service.adapter.web.error.ErrorCode;
import com.tubeplus.board_service.application.board.domain.Board;
import com.tubeplus.board_service.application.board.port.in.BoardUseCase;
import com.tubeplus.board_service.adapter.web.controller.board.vo.ReqMakeBoardBody;
import com.tubeplus.board_service.adapter.web.common.ApiResponse;
import com.tubeplus.board_service.adapter.web.controller.board.vo.BoardSearchType;

import com.tubeplus.board_service.application.board.port.in.BoardUseCase.BoardProperty;
import com.tubeplus.board_service.application.board.port.in.BoardUseCase.BoardProperty.BoardCommonProperty;
import com.tubeplus.board_service.application.board.port.in.BoardUseCase.BoardProperty.TimeLimitBoardProperty;
import com.tubeplus.board_service.application.board.port.in.BoardUseCase.MakeBoardForm;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "*")

@ApiTag(path = "/api/v1/board-service/boards", name = "Board API")
public class BoardController {

    private final BoardUseCase boardService;

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @Operation(summary = "게시판 생성", description = "게시판 생성, 생성된 게시판 id 반환")
    @PostMapping()
    public ApiResponse<Long> makeBoard
            (
                    @Valid @RequestBody ReqMakeBoardBody reqBody
            ) {

        MakeBoardForm form = reqBody.buildForm();
        log.info(form.toString());

        Board postedBoard = boardService.makeBoard(form);

        Long postedBoardId = postedBoard.getId();

        return ApiResponse.ofSuccess(postedBoardId);
    }


    @Operation(summary = "게시판 목록 조회", description = "community_id, status로 게시판 목록 조회")
    @GetMapping()
    public ApiResponse<List<Board>> readBoardList
            (
                    @RequestParam("community-id") Long communityId,
                    @RequestParam("board-search-type") BoardSearchType searchType,
                    @RequestParam(name = "board-name", required = false) String nameToSearch
            ) {

        log.info(communityId.toString());

        BoardUseCase.BoardListInfo findInfo
                = BoardUseCase.BoardListInfo.builder()
                .communityId(communityId)
                .visible(searchType.getVisible())
                .softDelete(searchType.getErase())
                .nameToSearch(nameToSearch)
                .build();
        log.info(findInfo.toString());


        List<Board> foundBoards = boardService.listCommuBoards(findInfo);


        return ApiResponse.ofSuccess(foundBoards);
    }


    @Operation(summary = "게시판 속성 조회", description = "특정 게시판 속성을 id로 조회, 게시판 설정페이지 표시에 사용")
    @GetMapping("/{id}")
    public ApiResponse<BoardProperty> readBoardProperty
            (
                    @PathVariable("id") Long boardId
            ) {
        log.info(boardId.toString());

        Board foundBoard
                = boardService.findBoard(boardId);

        BoardProperty boardProperty
                = BoardProperty.of(foundBoard);

        return ApiResponse.ofSuccess(boardProperty);
    }


    @Operation(summary = "게시판 공통 속성 변경")
    @PutMapping("/{boardId}/common-property")
    public ApiResponse updateBoardCommonProperty
            (
                    @PathVariable("boardId") Long boardId,
                    @RequestBody BoardCommonProperty updateReqBody
            ) {
        log.info(boardId.toString());

        boardService.updateBoardProperty(
                boardId, BoardProperty.of(updateReqBody, null)
        );

        return ApiResponse.ofSuccess(null);
    }


    @Operation(summary = "시간 제한 게시판 속성 변경")
    @PutMapping("/{id}/time-limit-property")
    public ApiResponse updateTimeLimitProperty
            (
                    @PathVariable("id") Long boardId,
                    @RequestBody ReqUpdateTimeLimitPropertyBody updateReqBody
            ) {

        if (updateReqBody.getLimitDateTime() == null)
            throw new BusinessException(ErrorCode.BAD_REQUEST);

        TimeLimitBoardProperty timeLimitProperty
                = updateReqBody.toDomain();

        boardService.updateBoardProperty(
                boardId,
                BoardProperty.of(null, timeLimitProperty)
        );

        return ApiResponse.ofSuccess(null);
    }


    @Operation(summary = "시간 제한 속성 제거")
    @DeleteMapping("/{id}/time-limit-property")
    public ApiResponse deleteTimeLimitProperty
            (
                    @PathVariable("id") Long boardId
            ) {

        boardService.updateBoardProperty(
                boardId,
                BoardProperty.of(null, TimeLimitBoardProperty.of(null))
        );

        return ApiResponse.ofSuccess(null);
    }


    @Operation(summary = "게시판 삭제", description = "특정 id의 게시판을 soft delete 처리")
    @DeleteMapping("/{boardId}")
    public ApiResponse deleteBoard
            (
                    @Valid @PathVariable("boardId")
                    @NotNull @Min(1) Long boardId
            ) {
        if (boardId == null || boardId < 1)
            throw new BusinessException(ErrorCode.BAD_REQUEST);


        boardService.softlyDeleteBoard(boardId);

        return ApiResponse.ofSuccess(null);
    }

}


//    private boolean haveNoUpdate(VoBoardProperty updateReq) {
//        //todo Field.get하면 접근제어자 문제 발생 -> 생각해보니 getter사용하는 방향으로 수정하면 작동할지도?, 그래도 안되면 reflection으로 접근제어자 무시하고 접근
//
//
//        //한개의 field라도 들어온게 있다면 update가 있는걸로 간주, false 리턴
//        for (Field updateField : updateReq.getClass().getFields()) {
//            Object fieldInstance = null;
//            try {
//                fieldInstance = updateField.get(updateReq);
//            } catch (NullPointerException e) {
//                log.error("bi");
//            } catch (Exception e) {
//                log.error("hi");
//            }
//
//            if (fieldInstance != null) {
//                log.error("asdlkfjasdlk");
//                return false;
//            }
//
//        }
//
//        return true;
//    }

