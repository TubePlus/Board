package com.tubeplus.board_service.application.posting.domain.posting;


import com.tubeplus.board_service.application.posting.port.out.CommentPersistent;
import com.tubeplus.board_service.application.posting.port.out.VotePersistent;
import com.tubeplus.board_service.application.posting.port.out.VotePersistent.FindVoteDto;
import com.tubeplus.board_service.adapter.web.error.BusinessException;
import com.tubeplus.board_service.adapter.web.error.ErrorCode;
import com.tubeplus.board_service.application.posting.domain.vote.Vote;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;


@Slf4j
@Data
@Builder
public class PostingViewInfo {

    private final String authorUuid;

    private final long voteCount;

    private final String contents;

    private final String title;

    private final Long userVoteId;

    private final boolean haveImage;


    public static PostingViewInfo of(Posting posting,
                                     String userUuid,
                                     VotePersistent votePersistence,
                                     CommentPersistent commentPersistence) {

        Vote userVote
                = getUserVote(posting, userUuid, votePersistence);

        return PostingViewInfo.builder()
                .authorUuid(posting.getAuthorUuid())
                .voteCount(posting.getVoteCount())
                .contents(posting.getContents())
                .title(posting.getTitle())
                .userVoteId(userVote.getId())
                .haveImage(posting.isWithImage())
                .build();
    }

    private static Vote getUserVote(Posting posting,
                                    String userUuid,
                                    VotePersistent votePersistence) {

        FindVoteDto dto
                = new FindVoteDto(posting.getId(), userUuid);

        Optional<Vote> optionalUserVote
                = votePersistence.findVote(dto)
                .ifExceptioned
                .throwOf(ErrorCode.FIND_ENTITY_FAILED);

        Vote userVote
                = optionalUserVote
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_RESOURCE));

        return userVote;
    }
}
