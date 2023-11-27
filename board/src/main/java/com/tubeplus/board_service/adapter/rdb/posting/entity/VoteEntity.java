package com.tubeplus.board_service.adapter.rdb.posting.entity;


import com.tubeplus.board_service.adapter.rdb.common.BaseEntity;
import com.tubeplus.board_service.adapter.rdb.posting.VoteTypeConverter;
import com.tubeplus.board_service.application.posting.domain.vote.Vote;
import com.tubeplus.board_service.application.posting.domain.vote.VoteType;
import com.tubeplus.board_service.application.posting.port.out.VotePersistable.SaveVoteDto;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)

@Entity
@Table(name = "vote")
public class VoteEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "posting_id", referencedColumnName = "id", nullable = false)
    private PostingEntity posting;

    @Column(name = "voter_uuid", nullable = false)
    private String voterUuid;

    @Setter
    @Convert(converter = VoteTypeConverter.class)
    @Column(name = "vote_type", nullable = false)
    private VoteType voteType;

    public static VoteEntity builtFrom(SaveVoteDto dto, PostingEntity votedPosting) {

        return VoteEntity.builder()
                .posting(votedPosting)
                .voterUuid(dto.getVoterUuid())
                .voteType(dto.getVoteType())
                .build();
    }

    public Vote buildDomain() {

        return Vote.builder()
                .id(id)
                .postingId(posting.getId())
                .voterUuid(voterUuid)
                .voteType(voteType)
                .build();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + getId() + ", " +
                "createdDateTime = " + getCreatedDateTime() + ", " +
                "updatedDateTime = " + getUpdatedDateTime() + ", " +
                "voterUuid = " + getVoterUuid() + ", " +
                "voteType = " + getVoteType() + ")";
    }

    @Override
    public int hashCode() {
        return Long.hashCode(getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof VoteEntity vote)) {
            return false;
        }

        return getId() == vote.getId() &&
                getCreatedDateTime().equals(vote.getCreatedDateTime()) &&
                getVoterUuid().equals(vote.getVoterUuid()) &&
                getVoteType().equals(vote.getVoteType());
    }
}
