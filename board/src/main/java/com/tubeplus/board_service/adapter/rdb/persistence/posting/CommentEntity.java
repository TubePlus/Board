package com.tubeplus.board_service.adapter.rdb.persistence.posting;

import com.tubeplus.board_service.adapter.rdb.config.BaseEntity;
import com.tubeplus.board_service.adapter.rdb.persistence.posting.dao.CommentJpaDataRepository;
import com.tubeplus.board_service.application.posting.domain.comment.Comment;
import com.tubeplus.board_service.application.posting.domain.comment.Comment.CommentViewInfo;
import com.tubeplus.board_service.application.posting.port.out.CommentPersistable.SaveCommentDto;
import jakarta.persistence.*;
import lombok.*;


//todo @JpaEntity(tableName="")으로 합치기


@Entity
@Table(name = "comment")

//todo @LombokEntityBoilerplate 로 합치기
@Getter
@Builder
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "posting_id", nullable = false)
    private long postingId;

    @Column(name = "contents", nullable = false, columnDefinition = "TEXT")
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "id", nullable = true)
    private CommentEntity parentComment;

    @Column(name = "commenter_uuid", nullable = false, length = 50)
    private String commenterUuid;


    public static CommentEntity builtFrom(SaveCommentDto dto, CommentEntity parentComment) {

        CommentEntityBuilder builder
                = CommentEntity.builder()
                .postingId(dto.getPostingId())
                .contents(dto.getContents())
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
