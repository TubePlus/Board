package com.tubeplus.board_service.application.posting.service;

import com.tubeplus.board_service.adapter.web.error.BusinessException;
import com.tubeplus.board_service.adapter.web.error.ErrorCode;
import com.tubeplus.board_service.application.posting.domain.posting.Posting;
import com.tubeplus.board_service.application.posting.domain.posting.PostingView;
import com.tubeplus.board_service.application.posting.port.in.PostingUseCase;
import com.tubeplus.board_service.application.posting.port.out.PostingPersistent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.tubeplus.board_service.application.posting.port.out.PostingPersistent.*;


@Slf4j
@RequiredArgsConstructor

@Service("Posting service")
public class PostingService implements PostingUseCase {

    private final PostingPersistent postingPersistence;

    private final VoteService voteService;
    private final CommentService commentService;


    @Override
    public PostingView readPostingView(long postingId, String userUuid) {

        Posting foundPosting
                = this.getPosting(postingId);

        //todo 읽음 집계처리 - 카프카

        return PostingView.builtFrom(
                foundPosting, userUuid,
                voteService, commentService);
    }

    @Override
    public Posting getPosting(long postingId) {

        Optional<Posting> optionalFound
                = postingPersistence.findPosting(postingId)
                .ifExceptioned
                .thenThrow(ErrorCode.FIND_ENTITY_FAILED);

        Posting foundPosting
                = optionalFound.orElseThrow(
                () -> new BusinessException(ErrorCode.NOT_FOUND_RESOURCE));

        return foundPosting;
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
    public void modifyPostingPinState(ModifyPinStateInfo modifyInfo) {

        UpdatePinStateDto dto = UpdatePinStateDto.builtFrom(modifyInfo);

        Posting updatedPosting
                = postingPersistence.updatePosting(dto)
                .ifExceptioned.thenThrow(ErrorCode.UPDATE_ENTITY_FAILED);

        if (updatedPosting == null)
            throw new BusinessException(ErrorCode.UPDATE_ENTITY_FAILED);
    }

    @Override
    public Posting modifyPostingArticle(long postingId, ModifyArticleForm form) {

        // User 권한점검, Posting 작성자인지
        String authorUuid
                = this.getPosting(postingId).getAuthorUuid();

        if (!authorUuid.equals(form.getUserUuid()))
            throw new BusinessException(ErrorCode.UNAUTHORIZED);


        // 글 내용, 제목 수정
        UpdateArticleDto dto = UpdateArticleDto.builtFrom(postingId, form);

        Posting modifiedPosting
                = postingPersistence.updatePosting(dto)
                .ifExceptioned.thenThrow(ErrorCode.UPDATE_ENTITY_FAILED);

        return modifiedPosting;
    }

    @Override
    public void modifyDeletePosting(ModifySoftDeleteInfo info) {

        UpdateSoftDeleteDto dto = UpdateSoftDeleteDto.builtFrom(info);

        Posting softDeletedPosting
                = postingPersistence.updatePosting(dto)
                .ifExceptioned.thenThrow(ErrorCode.SOFT_DELETE_ENTITY_FAILED);

        if (softDeletedPosting.isSoftDelete() != info.isSoftDelete())
            throw new BusinessException(ErrorCode.DELETE_ENTITY_FAILED);

    }

}
