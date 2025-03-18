package com.dbenavidess.gym_part_1.domain.repository;

import com.dbenavidess.gym_part_1.domain.model.Trainer;

import java.util.List;
import java.util.UUID;

public interface TrainerRepository {
    Trainer createTrainer(Trainer trainer);
    Trainer updateTrainer(Trainer trainer);
    void deleteTrainer(Trainer trainer);
    Trainer getTrainer(UUID id);
    List<Trainer> getAllTrainers();

}
