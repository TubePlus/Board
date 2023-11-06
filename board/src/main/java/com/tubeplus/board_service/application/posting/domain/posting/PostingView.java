package com.tubeplus.board_service.application.posting.domain.posting;


import com.tubeplus.board_service.application.posting.port.out.VotePersistent.FindVoteDto;
import com.tubeplus.board_service.application.posting.domain.vote.Vote;
import com.tubeplus.board_service.application.posting.service.CommentService;
import com.tubeplus.board_service.application.posting.service.VoteService;
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


    public static PostingView builtFrom(Posting posting,
                                        String userUuid,
                                        VoteService voteService,
                                        CommentService commentService) {

        FindVoteDto dto = FindVoteDto.of(posting.getId(), userUuid);

        Optional<Vote> optionalUserVote
                = voteService.findVote(dto);

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
