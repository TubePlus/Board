package com.tubeplus.board_service.adapter.rdb.persistence.posting;

import com.tubeplus.board_service.adapter.rdb.common.BaseEntity;
import com.tubeplus.board_service.application.posting.port.out.CommentPersistable.SaveCommentDto;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "comment")

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posting_id", referencedColumnName = "id", nullable = true)
    private PostingEntity posting;
//    @Column(name = "posting_id", nullable = false)
//    private long postingId;

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
