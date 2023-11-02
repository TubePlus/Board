package com.tubeplus.board_service.application.posting.service;

import com.tubeplus.board_service.application.posting.port.out.CommentPersistent;
import com.tubeplus.board_service.application.posting.port.out.VotePersistent;
import com.tubeplus.board_service.adapter.web.error.BusinessException;
import com.tubeplus.board_service.adapter.web.error.ErrorCode;
import com.tubeplus.board_service.application.posting.domain.posting.Posting;
import com.tubeplus.board_service.application.posting.domain.posting.PostingViewInfo;
import com.tubeplus.board_service.application.posting.port.in.PostingUseCase;
import com.tubeplus.board_service.application.posting.port.out.PostingPersistent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service("Posting service")
@RequiredArgsConstructor
@Slf4j
public class PostingService implements PostingUseCase {

    private final PostingPersistent postingPersistence;
    private final VotePersistent votePersistence;
    private final CommentPersistent commentPersistence;


    @Override
    public PostingViewInfo readPosting(long postingId, String userUuid) {

        Optional<Posting> found
                = postingPersistence.findPosting(postingId)
                .ifExceptioned
                .throwOf(ErrorCode.FIND_ENTITY_FAILED);

        Posting foundPosting
                = found.orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_RESOURCE));

        //todo 읽음 집계처리 - 카프카

        return PostingViewInfo.of(
                foundPosting, userUuid, votePersistence, commentPersistence
        );
    }

    @Override
    public List<PostingSimpleInfo> readMyPostingTitles(String userUuid) {
        return null;
    }

    @Override
    public List<PostingSimpleInfo> pagePostingTitles(Long boardId, PageDto dto) {
        return null;
    }

    @Override
    public List<PostingSimpleInfo> feedPostingTitles(Long boardId, FeedDto dto) {
        return null;
    }

    @Override
    public Long makePosting(MakePostingForm form) {
        return null;
    }

    @Override
    public void pinPosting(long id) {

    }

    @Override
    public Posting modifyPosting(ModifyPostingForm form) {
        return null;
    }

    @Override
    public void softDeletePosting(long postingId) {

        Boolean isDeleted
                = postingPersistence.softDeletePosting(postingId)
                .ifExceptioned
                .throwOf(ErrorCode.DELETE_ENTITY_FAILED);

        if (!isDeleted)
            throw new BusinessException(ErrorCode.DELETE_ENTITY_FAILED);

    }
}
