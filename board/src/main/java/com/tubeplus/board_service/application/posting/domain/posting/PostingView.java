package com.tubeplus.board_service.application.posting.domain.posting;


import com.tubeplus.board_service.adapter.web.error.ErrorCode;
import com.tubeplus.board_service.application.posting.port.out.CommentPersistable;
import com.tubeplus.board_service.application.posting.port.out.VotePersistable;
import com.tubeplus.board_service.application.posting.port.out.VotePersistable.FindVoteDto;
import com.tubeplus.board_service.application.posting.domain.vote.Vote;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;


@Slf4j
@Data
@Builder
public class PostingView {

    private final String authorUuid;

    private final long voteCount;

    private final String contents;

    private final String title;

    private final Long userVoteId;

    private final boolean withImage;


    public static PostingView madeFrom(Posting posting,
                                       String userUuid,
                                       VotePersistable votePersistence,
                                       CommentPersistable commentPersistence) {

        FindVoteDto dto = FindVoteDto.of(posting.getId(), userUuid);

        Optional<Vote> optionalUserVote
                = votePersistence.findVote(dto)
                .ifExceptioned.thenThrow(ErrorCode.FIND_ENTITY_FAILED);

        Long userVoteId
                = optionalUserVote.map(Vote::getId)
                .orElse(null);

        return PostingView.builder()
                .authorUuid(posting.getAuthorUuid())
                .voteCount(posting.getVoteCount())
                .contents(posting.getContents())
                .title(posting.getTitle())
                .userVoteId(userVoteId)
                .withImage(posting.isWithImage())
                .build();
    }


}
