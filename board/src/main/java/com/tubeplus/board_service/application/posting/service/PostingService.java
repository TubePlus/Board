package com.tubeplus.board_service.application.posting.service;

import com.tubeplus.board_service.adapter.web.error.BusinessException;
import com.tubeplus.board_service.adapter.web.error.ErrorCode;
import com.tubeplus.board_service.application.posting.domain.posting.PostingFeedData;
import com.tubeplus.board_service.application.posting.domain.posting.PostingPageView;
import com.tubeplus.board_service.application.posting.domain.posting.Posting;
import com.tubeplus.board_service.application.posting.domain.posting.PostingView;
import com.tubeplus.board_service.application.posting.port.in.PostingCommentUseCase;
import com.tubeplus.board_service.application.posting.port.in.PostingUseCase;
import com.tubeplus.board_service.application.posting.port.in.PostingVoteUseCase;
import com.tubeplus.board_service.application.posting.port.out.PostingEventPublishable;
import com.tubeplus.board_service.application.posting.port.out.PostingPersistable;
import com.tubeplus.board_service.global.Exceptionable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.LongSupplier;
import java.util.stream.Collectors;

import static com.tubeplus.board_service.application.posting.port.out.PostingPersistable.*;


@SuppressWarnings("UnnecessaryLocalVariable")
@Slf4j
@Service
@RequiredArgsConstructor
public class PostingService implements PostingUseCase {

    private final PostingVoteUseCase voteService;
    private final PostingCommentUseCase commentService;

    private final PostingPersistable postingPersistence;

    private final PostingEventPublishable eventPublisher;


    @Override
    public PostingView readPostingView(long postingId, String userUuid) {

        Posting foundPosting
                = this.getPosting(postingId);

        //todo 읽음 집계처리 - 카프카(log) - foundPosting.getBoardId() - 아래 함수에서 구현
        eventPublisher.publishPostingRead(foundPosting);

        return PostingView.madeFrom(
                foundPosting,
                userUuid,
                voteService,
                commentService
        );
    }


    @Override
    public Page<PostingPageView> pagePostingSimpleData(InfoToPagePostingData infoToPage) {

        /**/
        List<Posting> foundPagePostings;

        FindPostingsDto dto = FindPostingsDto.of(infoToPage);

        foundPagePostings = postingPersistence.findPostings(dto)
                .ifExceptioned.thenThrow(ErrorCode.FIND_ENTITY_FAILED);

        if (foundPagePostings.isEmpty())
            throw new BusinessException(
                    ErrorCode.NOT_FOUND_RESOURCE, "No postings to page found.");


        //count 쿼리 최적화 위한 함수형 변수
        LongSupplier countPostingsFunction
                = () -> postingPersistence.countPostings(dto.getFieldsFindCondition())
                .ifExceptioned.thenThrow(ErrorCode.COUNT_ENTITY_FAILED);


        /**/
        Page<PostingPageView> pagedPostingData;

        Page<Posting> pagedPostings //todo 첫페이지랑 마지막 언저리페이지들만 count 쿼리 날리도록 최적화 수정
                = PageableExecutionUtils.getPage // PageableExecutionUtils.getPage: count 쿼리 최적화 위해 사용
                (foundPagePostings, infoToPage.getPageReq(), countPostingsFunction);

        pagedPostingData
                = pagedPostings.map(
                posting ->
                        PostingPageView.builtFrom(
                                posting,
                                commentService.countComments(posting.getId())
                        )
        );

        return pagedPostingData;
    }


    @Override
    public Feed<PostingFeedData> feedPostingSimpleData(InfoToFeedPostingData infoToFeed) {

        FindPostingsDto findDto = FindPostingsDto.of(infoToFeed);

        /**/
        List<PostingFeedData> feedDataList;

        List<Posting> foundFeedPostings
                = postingPersistence.findPostings(findDto)
                .ifExceptioned.thenThrow(ErrorCode.FIND_ENTITY_FAILED);

        if (foundFeedPostings.isEmpty())
            throw new BusinessException(ErrorCode.NOT_FOUND_RESOURCE, "No postings to feed condition found.");

        feedDataList
                = foundFeedPostings.stream().map(
                posting ->
                        PostingFeedData.builtFrom(
                                posting,
                                commentService.countComments(posting.getId())
                        )
        ).collect(Collectors.toList());


        /**/
        Long lastCursoredId;

        Posting lastFoundPosting
                = foundFeedPostings.get(foundFeedPostings.size() - 1);

        lastCursoredId = lastFoundPosting.getId();


        /**/
        boolean hasNextFeed;

        findDto.getFieldsFindCondition()
                .setCursorId(lastCursoredId);

        hasNextFeed = Exceptionable.act(postingPersistence::existNextPosting, findDto)
                .ifExceptioned.thenThrow(new BusinessException(
                        ErrorCode.FIND_ENTITY_FAILED, "Failed to check if there is next posting to feed."
                ));


        /**/
        return Feed.of(
                feedDataList,
                lastCursoredId,
                hasNextFeed
        );
    }

    private Posting getPosting(long postingId) {

        Optional<Posting> optionalFound
                = postingPersistence.findPosting(postingId)
                .ifExceptioned
                .thenThrow(ErrorCode.FIND_ENTITY_FAILED);

        Posting foundPosting
                = optionalFound.orElseThrow(
                () -> new BusinessException(ErrorCode.NOT_FOUND_RESOURCE));

        return foundPosting;
    }

    // Create
    @Override
    public Long makePosting(MakePostingForm form) {

        SavePostingDto dto = SavePostingDto.builtFrom(form);

        Long madePostingId
                = postingPersistence.savePosting(dto)
                .ifExceptioned.thenThrow(ErrorCode.SAVE_ENTITY_FAILED);

        return madePostingId;
    }


    // Update
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

        /**/
        boolean checkUserNotAuthor;

        String authorUuid = this.getPosting(postingId).getAuthorUuid();

        checkUserNotAuthor = !authorUuid.equals(form.getUserUuid());

        if (checkUserNotAuthor)
            throw new BusinessException(ErrorCode.UNAUTHORIZED);


        /**/
        Posting modifiedPosting;

        UpdateArticleDto dto
                = UpdateArticleDto.builtFrom(postingId, form);

        modifiedPosting
                = postingPersistence.updatePosting(dto)
                .ifExceptioned.thenThrow(ErrorCode.UPDATE_ENTITY_FAILED);

        return modifiedPosting;
    }


    @Override
    public void modifyPostingDelete(ModifySoftDeleteInfo info) {

        UpdateSoftDeleteDto dto = UpdateSoftDeleteDto.builtFrom(info);

        Posting softDeletedPosting
                = postingPersistence.updatePosting(dto)
                .ifExceptioned.thenThrow(ErrorCode.SOFT_DELETE_ENTITY_FAILED);

        if (softDeletedPosting.isSoftDelete() != info.isSoftDelete())
            throw new BusinessException(ErrorCode.DELETE_ENTITY_FAILED);

    }


}
