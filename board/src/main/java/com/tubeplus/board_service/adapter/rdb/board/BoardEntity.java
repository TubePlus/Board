package com.tubeplus.board_service.adapter.rdb.board;


import com.tubeplus.board_service.adapter.rdb.common.BaseEntity;
import com.tubeplus.board_service.application.board.domain.Board;
import com.tubeplus.board_service.application.board.domain.BoardType;
import com.tubeplus.board_service.application.board.port.out.BoardPersistable.SaveBoardDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;


@Table(name = "board")
@Getter
@Setter
@Entity
@Builder(toBuilder = true)
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

    @Column(nullable = false, name = "soft_delete")
    private boolean softDelete;


    public static BoardEntity builtFrom(SaveBoardDto saveDto) {
        return BoardEntity.builder()
                .communityId(saveDto.getCommunityId())
                .boardName(saveDto.getBoardName())
                .boardType(saveDto.getBoardType())
                .boardDescription(saveDto.getBoardDescription())
                .visible(saveDto.isVisible())
                .limitDateTime(saveDto.getLimitDateTime())
                .softDelete(false)
                .build();
    }

    public Board buildDomain() {
        return Board.builder().
                id(id).
                communityId(communityId).
                boardName(boardName).
                boardType(boardType).
                boardDescription(boardDescription).
                visible(visible).
                limitDateTime(limitDateTime).
                softDelete(softDelete).
                build();
    }


    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true;

        if (obj instanceof BoardEntity that) {
            return Objects.equals(this.id, that.id)
                    && Objects.equals(this.communityId, that.communityId)
                    && Objects.equals(this.boardName, that.boardName)
                    && Objects.equals(this.boardType, that.boardType)
                    && Objects.equals(this.boardDescription, that.boardDescription)
                    && Objects.equals(this.visible, that.visible)
                    && Objects.equals(this.limitDateTime, that.limitDateTime)
                    && Objects.equals(this.softDelete, that.softDelete);
        } else
            return false;


    }
}