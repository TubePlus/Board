package com.tubeplus.board_service.adapter.rdb.persistence.posting;

import com.tubeplus.board_service.adapter.rdb.config.BaseEntity;
import com.tubeplus.board_service.application.posting.port.out.CommentPersistent.SaveCommentDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


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


}
