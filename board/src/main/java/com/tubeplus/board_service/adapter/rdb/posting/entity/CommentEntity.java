package com.tubeplus.board_service.adapter.rdb.posting.entity;

import com.tubeplus.board_service.adapter.rdb.common.BaseEntity;
import com.tubeplus.board_service.application.posting.port.out.CommentPersistable.SaveCommentDto;
import jakarta.persistence.*;
import lombok.*;



@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)

@Entity
@Table(name = "comment")
public class CommentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posting_id", referencedColumnName = "id", nullable = false)
    private PostingEntity posting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "id", nullable = true)
    private CommentEntity parentComment;

    @Setter
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "commenter_uuid", nullable = false, length = 50)
    private String commenterUuid;


    public static CommentEntity builtFrom(SaveCommentDto dto,
                                          PostingEntity postingEntity,
                                          CommentEntity parentComment) {

        return CommentEntity.builder()
                .posting(postingEntity)
                .content(dto.getContents())
                .parentComment(parentComment)
                .commenterUuid(dto.getCommenterUuid())
                .build();

    }

}
