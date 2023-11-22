package com.tubeplus.board_service.adapter.rdb.posting;

import com.tubeplus.board_service.adapter.rdb.posting.dao.PostingJpaDataRepository;
import com.tubeplus.board_service.adapter.rdb.posting.dao.VoteJpaDataRepository;
import com.tubeplus.board_service.adapter.rdb.posting.entity.PostingEntity;
import com.tubeplus.board_service.adapter.rdb.posting.entity.VoteEntity;
import com.tubeplus.board_service.application.posting.domain.posting.Posting;
import com.tubeplus.board_service.global.Exceptionable;
import com.tubeplus.board_service.application.posting.domain.vote.Vote;
import com.tubeplus.board_service.application.posting.port.out.VotePersistable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Component("votePersistence")
public class VotePersistence
        implements VotePersistable {


    private final VoteJpaDataRepository voteJpaDataRepo;
    private final PostingJpaDataRepository postingJpaDataRepo;


    @Override
    public Exceptionable<Optional<Vote>, FindVoteDto> findVote(FindVoteDto findVoteDto) {

        return Exceptionable.act(dto -> {

            Optional<VoteEntity> foundEntity
                    = voteJpaDataRepo.findByPostingIdAndVoterUuid
                    (dto.getPostingId(), dto.getVoterUuid());

            Optional<Vote> foundVote
                    = foundEntity.map(VoteEntity::buildDomain);

            return foundVote;

        }, findVoteDto);
    }

    @Override
    public Exceptionable<Boolean, Vote> updateVote(Vote updateInfo) {

        return Exceptionable.act(info ->
        {
            VoteEntity toUpdate
                    = voteJpaDataRepo.findById(info.getId())
                    .orElseThrow(() -> new RuntimeException("vote is not found."));

            toUpdate.setVoteType(info.getVoteType());

            VoteEntity updated
                    = voteJpaDataRepo.save(toUpdate);

            return updated.equals(toUpdate);

        }, updateInfo);

    }

    @Override
    public Exceptionable<Vote, SaveVoteDto> saveVote(SaveVoteDto saveDto) {

        return Exceptionable.act(dto -> {


            PostingEntity votedPosting
                    = postingJpaDataRepo.findById(dto.getPostingId())
                    .orElseThrow(() -> new RuntimeException("posting is not found."));

            VoteEntity entityToSave
                    = VoteEntity.builtFrom(dto, votedPosting);

            VoteEntity savedEntity
                    = voteJpaDataRepo.save(entityToSave);

            return savedEntity.buildDomain();

        }, saveDto);
    }


    @Override
    public Exceptionable<Boolean, Long> deleteVote(Long voteId) {

        return Exceptionable.act(dto -> {

            voteJpaDataRepo.deleteById(dto);

            return voteJpaDataRepo.findById(dto).isEmpty();

        }, voteId);
    }

    @Override
    public Exceptionable<Optional<Vote>, Long> findVote(Long voteId) {
        return Exceptionable.act(id -> {
            return voteJpaDataRepo.findById(id).map(VoteEntity::buildDomain);
        }, voteId);
    }

    @Override
    public Exceptionable<Posting, Long> getVotedPosting(Long voteId) {

        return Exceptionable.act(dto -> {

            VoteEntity voteEntity
                    = voteJpaDataRepo.findById(dto)
                    .orElseThrow(() -> new RuntimeException("vote is not found."));

            Posting votedPosting
                    = voteEntity.getPosting()
                    .buildDomain();

            return votedPosting;

        }, voteId);

    }

    @Override
    public Exceptionable<Long, Long> getTotalVote(Long postingId) {

        return Exceptionable.act(id -> {

            PostingEntity posting
                    = postingJpaDataRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("posting is not found."));

            return posting.getVoteCount();

        }, postingId);
    }
}

