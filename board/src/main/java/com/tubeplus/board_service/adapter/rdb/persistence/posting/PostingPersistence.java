package com.tubeplus.board_service.adapter.rdb.persistence.posting;

import com.tubeplus.board_service.adapter.rdb.persistence.posting.dao.PostingJpaDataRepository;
import com.tubeplus.board_service.adapter.rdb.persistence.posting.dao.PostingQDslRepositoryCustom;
import com.tubeplus.board_service.global.Exceptionable;
import com.tubeplus.board_service.application.posting.domain.posting.Posting;
import com.tubeplus.board_service.application.posting.port.out.PostingPersistent;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Function;


@SuppressWarnings("ConstantConditions")
@Slf4j
@Component
@RequiredArgsConstructor
public class PostingPersistence implements PostingPersistent {

    private final PostingJpaDataRepository jpaDataRepo;
    private final PostingQDslRepositoryCustom queryDslRepo;

    private final EntityManager em;
    private final ModelMapper modelMapper;


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


    @Override
    @Transactional(readOnly = true)
    public Exceptionable<Page<Posting>, PagePostingsDto> CursorPostings(CursorPostingsDto dto) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Exceptionable<Page<Posting>, PagePostingsDto> pagePostings(PagePostingsDto pagePostingsDto) {

        Function<PagePostingsDto, Page<Posting>> pagePostings
                = (dto) -> {

            Page<PostingEntity> pagedEntities
                    = queryDslRepo.pagePostingEntities(dto);

            Page<Posting> pagedPostings
                    = pagedEntities.map(PostingEntity::buildDomain);

            return pagedPostings;
        };

        return new Exceptionable<>(pagePostings, pagePostingsDto);
    }


    @Override
    @Transactional
    public Exceptionable<Posting, BaseUpdatePostingDto> updatePosting(BaseUpdatePostingDto updateDto) {
        //todo 영속성 컨텍스트 공부해보고, 최적화 가능하다면 queryDsl로 바꾸기 - 하나의 엔티티만 컨텍스트 초기화 되는지 확인

        Function<BaseUpdatePostingDto, Posting> updatePosting
                = (dto) -> {
            // dto로 수정 요청된 필드 엔티티에 반영
            PostingEntity entityToUpdate
                    = em.find(PostingEntity.class, dto.getPostingId());

            Class dtoClazz = dto.getClass();
            modelMapper.map(dtoClazz.cast(dto), entityToUpdate);


            // 엔티티 수정, 수정된 엔티티로 도메인 객체 생성 및 반환
            PostingEntity updatedEntity
                    = jpaDataRepo.save(entityToUpdate);

            Posting updatedPosting
                    = updatedEntity.buildDomain();

            return updatedPosting;

        };

        return new Exceptionable<>(updatePosting, updateDto);
    }


}