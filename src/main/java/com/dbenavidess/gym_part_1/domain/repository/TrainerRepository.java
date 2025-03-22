package com.dbenavidess.gym_part_1.domain.repository;

import com.dbenavidess.gym_part_1.domain.model.Trainer;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public interface TrainerRepository {
    Trainer createTrainer(Trainer trainer);
    Trainer updateTrainer(Trainer trainer);
    void deleteTrainer(UUID id);
    Trainer getTrainer(UUID id);

    List<Trainer> search(Predicate<Trainer> p);

    List<Trainer> getAllTrainers();

}
