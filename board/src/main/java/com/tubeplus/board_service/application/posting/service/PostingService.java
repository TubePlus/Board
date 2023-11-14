package com.tubeplus.board_service.application.posting.service;

import com.tubeplus.board_service.adapter.web.error.BusinessException;
import com.tubeplus.board_service.adapter.web.error.ErrorCode;
import com.tubeplus.board_service.application.posting.domain.posting.Posting;
import com.tubeplus.board_service.application.posting.domain.posting.PostingView;
import com.tubeplus.board_service.application.posting.port.in.PostingUseCase;
import com.tubeplus.board_service.application.posting.port.out.CommentPersistent;
import com.tubeplus.board_service.application.posting.port.out.PostingPersistent;
import com.tubeplus.board_service.application.posting.port.out.VotePersistent;
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

import static com.tubeplus.board_service.application.posting.port.out.PostingPersistent.*;


@Slf4j
@RequiredArgsConstructor

@Service("Posting service")
public class PostingService implements PostingUseCase {

    private final PostingPersistent postingPersistence;
    private final VotePersistent votePersistence;
    private final CommentPersistent commentPersistent;


    @Override
    public PostingView readPostingView(long postingId, String userUuid) {

        Posting foundPosting
                = this.getPosting(postingId);

        //todo 읽음 집계처리 - 카프카

        return PostingView.madeFrom(
                foundPosting, userUuid, votePersistence, commentPersistent
        );
    }


    @Override
    public Page<PostingSimpleData> pagePostingSimpleData(InfoToPagePostingData infoToPage) {

        //Page 생성 위한 db 조회
        FindPostingsDto dto = FindPostingsDto.of(infoToPage);

        List<Posting> foundPostingsToPage
                = postingPersistence.findPostings(dto)
                .ifExceptioned.thenThrow(ErrorCode.FIND_ENTITY_FAILED);

        if (foundPostingsToPage.isEmpty())
            throw new BusinessException(
                    ErrorCode.NOT_FOUND_RESOURCE, "No postings to page found."
            );

        //count 쿼리 최적화 위한 함수형 변수
        LongSupplier countPostingsFunction
                = () -> postingPersistence.countPostings(dto.getConditionByFields())
                .ifExceptioned.thenThrow(ErrorCode.COUNT_ENTITY_FAILED);


        //Page 생성
        Page<Posting> pagedPostings //todo 첫페이지랑 마지막 언저리페이지들만 count 쿼리 날리도록 최적화 수정
                = PageableExecutionUtils.getPage // PageableExecutionUtils.getPage: count 쿼리 최적화 위해 사용
                (foundPostingsToPage, infoToPage.getPageReq(), countPostingsFunction);

        Page<PostingSimpleData> pagedPostingData
                = pagedPostings.map(PostingSimpleData::builtFrom);

        return pagedPostingData;
    }


    @Override
    public Feed<PostingSimpleData> feedPostingSimpleData(InfoToFeedPostingData infoToFeed) {

        FindPostingsDto findDto;
        findDto = FindPostingsDto.of(infoToFeed);


        List<PostingSimpleData> postingDataToFeed;

        List<Posting> foundPostingsForFeed;
        foundPostingsForFeed = postingPersistence.findPostings(findDto)
                .ifExceptioned.thenThrow(ErrorCode.FIND_ENTITY_FAILED);

        if (foundPostingsForFeed.isEmpty())
            throw new BusinessException(ErrorCode.NOT_FOUND_RESOURCE, "No postings to feed condition found.");

        postingDataToFeed = foundPostingsForFeed.stream().map(PostingSimpleData::builtFrom)
                .collect(Collectors.toList());


        Long lastCursoredId;
        lastCursoredId = foundPostingsForFeed.get(foundPostingsForFeed.size() - 1).getId();


        boolean hasNextFeed;

        findDto.getConditionByFields().setCursorId(lastCursoredId);

        hasNextFeed = Exceptionable.act(postingPersistence::existNextPosting, findDto)
                .ifExceptioned.thenThrow(new BusinessException(
                        ErrorCode.FIND_ENTITY_FAILED, "Failed to check if there is next posting to feed."));


        return Feed.of(postingDataToFeed, lastCursoredId, hasNextFeed);
    }


    // Create
    @Override
    public Long makePosting(MakePostingForm form) {
        return null;
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

        // User 권한점검: Posting 작성자인지 확인
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
}
