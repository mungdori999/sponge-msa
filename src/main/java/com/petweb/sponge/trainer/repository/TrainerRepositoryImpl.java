package com.petweb.sponge.trainer.repository;

import com.petweb.sponge.trainer.domain.Trainer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TrainerRepositoryImpl implements TrainerRepository{
    private final TrainerJpaRepository trainerJpaRepository;
    @Override
    public Optional<Trainer> findByEmail(String email) {
        return trainerJpaRepository.findByEmail(email).map(TrainerEntity::toModel);
    }

    @Override
    public Optional<Trainer> findShortById(Long id) {
        return trainerJpaRepository.findById(id).map(TrainerEntity::toModel);
    }


    @Override
    public List<Trainer> findShortByIdList(List<Long> trainerIdList) {
        return trainerJpaRepository.findShortByIdList(trainerIdList).stream().map(TrainerEntity::toModel).collect(Collectors.toList());
    }

    @Override
    public Optional<Trainer> findById(Long id) {
        return trainerJpaRepository.findTrainerById(id).map(TrainerEntity::toModel);
    }

    @Override
    public Trainer save(Trainer trainer) {
        return trainerJpaRepository.save(TrainerEntity.from(trainer)).toModel();
    }

    @Override
    public void initTrainer(Long id) {
        trainerJpaRepository.initTrainer(id);
    }

}
