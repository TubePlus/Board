package com.tubeplus.board_service.external.rdb.entity;


import com.tubeplus.board_service.domain.board.model.Board;
import com.tubeplus.board_service.domain.board.model.BoardType;
import com.tubeplus.board_service.domain.board.port.out.BoardPersistent;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;


@Table(name = "board")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "community_id")
    private Long communityId;

    @Column(nullable = false, name = "board_name", length = 50)
    private String boardName;

    @Column(nullable = false, name = "board_type", length = 3)
    private BoardType boardType;

    @Column(nullable = false, name = "board_description", length = 100)
    private String boardDescription;

    @Column(nullable = false, name = "visible")
    private boolean visible;

    @Column(nullable = true, name = "limit_datetime")
    private LocalDateTime limitDateTime;

    @Column(nullable = false, name = "erase")
    private boolean erase;


    public static BoardEntity builtOf(BoardPersistent.SaveDto saveDto) {
        return BoardEntity.builder()
                .communityId(saveDto.getCommunityId())
                .boardName(saveDto.getBoardName())
                .boardType(saveDto.getBoardType())
                .boardDescription(saveDto.getBoardDescription())
                .visible(saveDto.isVisible())
                .limitDateTime(saveDto.getLimitDateTime())
                .erase(false)
                .build();
    }

    public Board buildBoard() {
        return Board.builder().
                id(id).
                communityId(communityId).
                boardName(boardName).
                boardType(boardType).
                boardDescription(boardDescription).
                visible(visible).
                limitDateTime(limitDateTime).
                erase(erase).
                build();
    }


}