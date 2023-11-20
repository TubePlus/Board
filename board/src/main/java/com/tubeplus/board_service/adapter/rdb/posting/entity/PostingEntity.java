package com.tubeplus.board_service.adapter.rdb.posting.entity;

import com.tubeplus.board_service.adapter.rdb.board.BoardEntity;
import com.tubeplus.board_service.adapter.rdb.common.BaseEntity;
import com.tubeplus.board_service.application.posting.domain.posting.Posting;
import com.tubeplus.board_service.application.posting.port.out.PostingPersistable.SavePostingDto;
import jakarta.persistence.*;
import lombok.*;


@Getter
@ToString
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)

@Entity
@Table(name = "posting")
public class PostingEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "author_uuid", nullable = false, length = 50)
    private String authorUuid;

    @Column(name = "vote_count", nullable = false)
    @Builder.Default
    private long voteCount = 0;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "board_posting_list",
            joinColumns = @JoinColumn(name = "board_id"),
            inverseJoinColumns = @JoinColumn(name = "id")
    )
    private BoardEntity board;

    @Column(name = "pin", nullable = false)
    private boolean pin;

    @Column(name = "contents", nullable = false, columnDefinition = "TEXT")
    private String contents;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "soft_delete", nullable = false)
    private boolean softDelete;

    @Column(name = "with_image", nullable = false)
    private boolean withImage;


    public static PostingEntity builtFrom(SavePostingDto dto, BoardEntity postingBoard) {

        return PostingEntity.builder()
                .authorUuid(dto.getAuthorUuid())
                .voteCount(0)
                .board(postingBoard)
                .pin(false)
                .contents(dto.getContents())
                .title(dto.getTitle())
                .softDelete(false)
                .withImage(dto.isWithImage())//
                .build();
    }

    public static PostingEntity builtFrom(Posting posting, BoardEntity postingBoard) {

        return PostingEntity.builder()
                .id(posting.getId())
                .authorUuid(posting.getAuthorUuid())
                .voteCount(posting.getVoteCount())
                .board(postingBoard)
                .pin(posting.isPin())
                .contents(posting.getContents())
                .title(posting.getTitle())
                .softDelete(posting.isSoftDelete())
                .withImage(posting.isWithImage())
                .build();
    }


    public Posting buildDomain() {

        return Posting.builder()
                .id(id)
                .authorUuid(authorUuid)
                .voteCount(voteCount)
                .boardId(board.getId())
                .pin(pin)
                .contents(contents)
                .title(title)
                .softDelete(softDelete)
                .withImage(withImage)
                .build();
    }


}