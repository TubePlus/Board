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

    @Column(name = "posting_id", nullable = false)
    private long postingId;

    @Setter
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "id", nullable = true)
    private CommentEntity parentComment;

    @Column(name = "commenter_uuid", nullable = false, length = 50)
    private String commenterUuid;


    public static CommentEntity builtFrom(SaveCommentDto dto, CommentEntity parentComment) {

        CommentEntityBuilder builder
                = CommentEntity.builder()
                .postingId(dto.getPostingId())
                .content(dto.getContents())
                .parentComment(parentComment)
                .commenterUuid(dto.getCommenterUuid());

        return builder.build();

    }

//    public Comment toDomain(CommentJpaDataRepository jpaDataRepo) {

//        return Comment.of(
//                id,
//                CommentViewInfo.builder()
//                        .postingId(postingId)
//                        .parentId(parentComment == null ? null : parentComment.id)
//                        .hasChild(parentComment != null && )
//                        .contents(contents)
//                        .commenterUuid(commenterUuid)
//                        .build()
//        );
//    }


}
