package com.tubeplus.board_service.adapter.rdb.persistence.posting;

import com.tubeplus.board_service.adapter.rdb.persistence.posting.dao.PostingJpaDataRepository;
import com.tubeplus.board_service.adapter.rdb.persistence.posting.dao.PostingQDslRepositoryCustom;
import com.tubeplus.board_service.global.Exceptionable;
import com.tubeplus.board_service.application.posting.domain.posting.Posting;
import com.tubeplus.board_service.application.posting.port.out.PostingPersistent;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Function;


@Slf4j
@Component
@RequiredArgsConstructor
public class PostingPersistence implements PostingPersistent {

    private final PostingJpaDataRepository jpaDataRepo;
    private final PostingQDslRepositoryCustom queryDslRepo;

    private final EntityManager em;


    @Override
    public Exceptionable<Optional<Posting>, Long> findPosting(final long postingId) {

        Function<Long, Optional<Posting>> findPostingById =
                id -> {
                    Optional<PostingEntity> optionalEntity
                            = jpaDataRepo.findById(id);

                    Optional<Posting> optionalPosting
                            = optionalEntity.map(PostingEntity::buildDomain);

                    return optionalPosting;
                };

        return new Exceptionable<>(findPostingById, postingId);
    }

    @Override
    public Exceptionable<Boolean, Long> softDeletePosting(long postingId) {

        Function<Long, Boolean> softDeletePostingById =
                id -> {
                    long updatedColumns
                            = queryDslRepo.updateSoftDelete(id);
                    log.info("updatedColumns: {}", updatedColumns);
                    return updatedColumns != 0;
                };

        return new Exceptionable<>(softDeletePostingById, postingId);
    }

    @Override
    public Exceptionable<Boolean, Long> changePinState(long postingId) {
        Function<Long, Boolean> changePinStateById =
                id -> {
                    long updatedColumns
                            = queryDslRepo.updatePinReversed(id);

                    return updatedColumns == 1;
                };

        return new Exceptionable<>(changePinStateById, postingId);
    }

    @Override
    @Transactional
    public Exceptionable<Posting, UpdateWritingDto> updatePostingWriting(final UpdateWritingDto updateWritingDto) {

        Function<UpdateWritingDto, Posting> updatePostingWriting =
                (dto) -> {

                    PostingEntity postingEntity
                            = em.find(PostingEntity.class, dto.getPostingId());

                    if (dto.getTitle() != null)
                        postingEntity.setTitle(dto.getTitle());
                    if (dto.getContents() != null)
                        postingEntity.setContents(dto.getContents());


                    PostingEntity updatedEntity
                            = jpaDataRepo.save(postingEntity);

                    return updatedEntity.buildDomain();
                };

        return new Exceptionable<Posting, UpdateWritingDto>
                (updatePostingWriting, updateWritingDto);
    }
}