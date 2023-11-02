package com.tubeplus.board_service.adapter.rdb.persistence.posting;

import com.tubeplus.board_service.adapter.rdb.config.BaseEntity;
import com.tubeplus.board_service.application.posting.domain.posting.Posting;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "posting")
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostingEntity extends BaseEntity {
    //todo 수정된 erd 반영하기
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "author_uuid", nullable = false)
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

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "erase", nullable = false)
    private boolean erase;


    public Posting buildDomain() {//todo 수정

        return Posting.builder()
                .id(id)
                .authorUuid(authorUuid)
                .voteCount(voteCount)
                .boardId(boardId)
                .pin(pin)
                .contents(contents)
                .title(title)
                .erase(erase)
                .withImage(true)//todo 수정하기
                .build();
    }

}