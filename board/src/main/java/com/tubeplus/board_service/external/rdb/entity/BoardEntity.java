package com.tubeplus.board_service.external.rdb.entity;


import com.tubeplus.board_service.domain.board.model.Board;
import com.tubeplus.board_service.domain.board.model.BoardType;
import com.tubeplus.board_service.domain.board.port.out.BoardPersistent;
import jakarta.persistence.*;
import lombok.*;

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

    @Column(nullable = false, name = "board_name")
    private String boardName;

    @Column(nullable = false, name = "board_type")
    private BoardType boardType;

    @Column(nullable = false, name = "board_description")
    private String boardDescription;

    @Column(nullable = false, name = "visible")
    private boolean visible;

    @Column(nullable = true, name = "limit_datetime")
    private LocalDateTime limitDateTime;

    @Column(nullable = false, name = "erase")
    private boolean erase;


    public static BoardEntity builtOf(BoardPersistent.SaveDto data) {
        return BoardEntity.builder()
                .communityId(data.getCommunityId())
                .boardName(data.getBoardName())
                .boardType(data.getBoardType())
                .boardDescription(data.getBoardDescription())
                .visible(data.isVisible())
                .limitDateTime(data.getLimitDateTime())
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