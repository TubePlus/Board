package com.tubeplus.board_service.adapter.rdb.posting;

import com.tubeplus.board_service.adapter.rdb.board.BoardEntity;
import com.tubeplus.board_service.adapter.rdb.posting.dao.PostingJpaDataRepository;
import com.tubeplus.board_service.adapter.rdb.posting.dao.PostingQDslRepositoryCustom;
import com.tubeplus.board_service.adapter.rdb.posting.entity.PostingEntity;
import com.tubeplus.board_service.application.posting.port.out.PostingPersistable.FindPostingsDto.FieldsFindCondition;
import com.tubeplus.board_service.global.Exceptionable;
import com.tubeplus.board_service.application.posting.domain.posting.Posting;
import com.tubeplus.board_service.application.posting.port.out.PostingPersistable;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;


@SuppressWarnings({"ConstantConditions", "UnnecessaryLocalVariable"})
@Slf4j
@RequiredArgsConstructor
@Component
@Transactional
public class PostingPersistence implements PostingPersistable {

    private final PostingJpaDataRepository jpaDataRepo;
    private final PostingQDslRepositoryCustom queryDslRepo;

    private final EntityManager em;
    private final ModelMapper modelMapper;


    // both used in query and command
    @Override
    @Transactional(readOnly = true)
    public Exceptionable<Optional<Posting>, Long> findPosting(final long postingId) {

        Function<Long, Optional<Posting>> findPostingById
                = id -> {

            Optional<PostingEntity> optionalEntity
                    = jpaDataRepo.findById(id);

            Optional<Posting> optionalPosting
                    = optionalEntity.map(PostingEntity::buildDomain);

            return optionalPosting;
        };

        return new Exceptionable<>(findPostingById, postingId);
    }


    // queries
    @Override
    @Transactional(readOnly = true)
    public boolean existNextPosting(FindPostingsDto findDto) {

        return queryDslRepo.existNextPosting(findDto);

    }

    @Override
    @Transactional(readOnly = true)
    public Exceptionable<Long, FieldsFindCondition> countPostings(FieldsFindCondition condition) {

        return Exceptionable.act(queryDslRepo::countPostingEntities, condition);
    }

    @Override
    @Transactional(readOnly = true)
    public Exceptionable<List<Posting>, FindPostingsDto> findPostings(FindPostingsDto findDto) {

        Function<FindPostingsDto, List<Posting>> findPostings
                = (dto) -> {

            List<PostingEntity> foundPostingEntities
                    = queryDslRepo.findPostingEntities(dto);

            List<Posting> foundPostings
                    = foundPostingEntities.stream()
                    .map(PostingEntity::buildDomain)
                    .collect(Collectors.toList());

            return foundPostings;
        };

        return new Exceptionable<>(findPostings, findDto);
    }

    @Override
    public Exceptionable<Boolean, Object> updateVoteCount(Long postingId, Integer voteDiff) {

        return Exceptionable.act(() ->
        {
            PostingEntity postingEntity
                    = em.find(PostingEntity.class, postingId);

            postingEntity.setVoteCount(postingEntity.getVoteCount() + voteDiff);

            log.info("postingEntity.getVoteCount() : " + postingEntity.getVoteCount());

            jpaDataRepo.save(postingEntity);

            return true;
        });
    }


    // commands
    @Override
    public Exceptionable<Long, SavePostingDto> savePosting(SavePostingDto savePostingDto) {

        return Exceptionable.act(dto -> {

            BoardEntity postingBoard
                    = em.find(BoardEntity.class, dto.getBoardId());

            PostingEntity postingEntity
                    = PostingEntity.builtFrom(dto, postingBoard);


            PostingEntity savedEntity
                    = jpaDataRepo.save(postingEntity);

            return savedEntity.getId();

        }, savePostingDto);

    }


    @Override
    public Exceptionable<Posting, BaseUpdatePostingDto> updatePosting(BaseUpdatePostingDto updateDto) {

        return Exceptionable.act(dto ->
        {
            // dto로 요청된 필드 수정 엔티티에 반영
            PostingEntity entityToUpdate
                    = em.find(PostingEntity.class, dto.getPostingId());

            modelMapper.map(dto, entityToUpdate);

            // 엔티티 수정, 수정된 엔티티로 도메인 생성 및 반환
            PostingEntity updatedEntity
                    = jpaDataRepo.save(entityToUpdate);

            Posting updatedPosting
                    = updatedEntity.buildDomain();

            return updatedPosting;

        }, updateDto);

    }


    @Override
    public Exceptionable<Long, Long> getPostingCommuId(Long postingId) {

        return Exceptionable.act(id ->
        {
            log.info(String.valueOf(id));
            PostingEntity postingEntity
                    = jpaDataRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("posting is not found."));

            return postingEntity.getBoard().getCommunityId();

        }, postingId);

    }

}
