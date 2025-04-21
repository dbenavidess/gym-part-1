package com.dbenavidess.gym_part_1.domain.repository;

import com.dbenavidess.gym_part_1.domain.model.Trainee;
import com.dbenavidess.gym_part_1.domain.model.Trainer;

import java.util.List;
import java.util.UUID;

public interface TrainerRepository {
    Trainer createTrainer(Trainer trainer);
    Trainer updateTrainer(Trainer trainer);
    void deleteTrainer(UUID id);
    Trainer getTrainer(UUID id);
    Trainer getByUsername(String username);

    List<Trainer> getAllTrainers();
    List<Trainee> addTraineeToTrainer(UUID traineeId, UUID trainerId);

}
