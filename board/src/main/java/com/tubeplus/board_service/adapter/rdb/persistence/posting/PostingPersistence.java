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


@Slf4j
@Component
@RequiredArgsConstructor
public class PostingPersistence implements PostingPersistent {

    private final PostingJpaDataRepository jpaDataRepo;
    private final PostingQDslRepositoryCustom queryDslRepo;

    private final EntityManager em;


    @Override
    public Exceptionable<Optional<Posting>, Long> findPosting(final long postingId) {

        return new Exceptionable<>(this::findPostingById, postingId);
    }

    protected Optional<Posting> findPostingById(final long postingId) {

        Optional<PostingEntity> optionalEntity
                = jpaDataRepo.findById(postingId);

        Optional<Posting> optionalPosting
                = optionalEntity.map(PostingEntity::buildDomain);

        return optionalPosting;
    }


    @Override
    public Exceptionable<Boolean, Long> softDeletePosting(long postingId) {

        return new Exceptionable<>(this::softDeletePostingById, postingId);
    }

    protected Boolean softDeletePostingById(Long postingId) {

        long updatedColumns
                = queryDslRepo.updateSoftDelete(postingId);

        return updatedColumns == 1;
    }


    @Override
    public Exceptionable<Boolean, Long> changePinState(long postingId) {

        return new Exceptionable<>(this::changePinStateById, postingId);
    }

    protected Boolean changePinStateById(Long id) {

        long updatedColumns
                = queryDslRepo.updatePinReversed(id);

        return updatedColumns == 1;
    }

    @Override
    @Transactional
    public Exceptionable<Posting, UpdateWritingDto> updatePostingWriting(final UpdateWritingDto updateWritingDto) {
        return new Exceptionable<Posting, UpdateWritingDto>(
                (dto) -> {

                    PostingEntity postingEntity
                            = em.find(PostingEntity.class, dto.getPostingId());

                    if (dto.getTitle() != null)
                        postingEntity.setTitle(dto.getTitle());

                    if (dto.getContents() != null)
                        postingEntity.setContents(dto.getContents());

                    return postingEntity.buildDomain();

                }
                , updateWritingDto);
    }
}