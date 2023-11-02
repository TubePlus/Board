package com.tubeplus.board_service.adapter.rdb.persistence.posting;

import com.tubeplus.board_service.adapter.rdb.persistence.posting.dao.PostingJpaDataRepository;
import com.tubeplus.board_service.adapter.rdb.persistence.posting.dao.PostingQDslRepositoryCustom;
import com.tubeplus.board_service.global.Exceptionable;
import com.tubeplus.board_service.application.posting.domain.posting.Posting;
import com.tubeplus.board_service.application.posting.port.out.PostingPersistent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Slf4j
@Component
@RequiredArgsConstructor
public class PostingPersistence implements PostingPersistent {

    private final PostingJpaDataRepository jpaDataRepo;
    private final PostingQDslRepositoryCustom queryDslRepo;


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
    public Exceptionable<Boolean, Long> pinPosting(long id) {

        return new Exceptionable<>(this::pinPostingById, id);
    }

    protected Boolean pinPostingById(Long id) {

        long updatedColumns
                = queryDslRepo.updatePostingPinned(id);

        return updatedColumns == 1;
    }
}