package com.tubeplus.board_service.adapter.rdb.persistence.posting;

import com.tubeplus.board_service.adapter.rdb.common.BaseEntity;
import com.tubeplus.board_service.application.posting.domain.posting.Posting;
import com.tubeplus.board_service.application.posting.port.out.PostingPersistable.SavePostingDto;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "posting")

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostingEntity extends BaseEntity {

    //todo vote, comment와 one to many 관계로 매핑하기
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "author_uuid", nullable = false, length = 50)
    private String authorUuid;

    @Column(name = "vote_count", nullable = false)
    @Builder.Default
    private long voteCount = 0;

    @Column(name = "board_id", nullable = false)
    private long boardId;

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

    public static PostingEntity builtFrom(SavePostingDto dto) {

        return PostingEntity.builder()
                .authorUuid(dto.getAuthorUuid())
                .voteCount(0)
                .boardId(dto.getBoardId())
                .pin(false)
                .contents(dto.getContents())
                .title(dto.getTitle())
                .softDelete(false)
                .withImage(dto.isWithImage())//
                .build();
    }


    public Posting buildDomain() {

        return Posting.builder()
                .id(id)
                .authorUuid(authorUuid)
                .voteCount(voteCount)
                .boardId(boardId)
                .pin(pin)
                .contents(contents)
                .title(title)
                .softDelete(softDelete)
                .withImage(withImage)
                .build();
    }

}