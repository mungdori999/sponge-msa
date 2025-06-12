package com.petweb.sponge.trainer.repository;

import com.petweb.sponge.trainer.domain.Trainer;

import java.util.Optional;

public interface TrainerQueryDslRepository {
    Optional<TrainerEntity> findTrainerById(Long id);

    void initTrainer(Long id);
}
