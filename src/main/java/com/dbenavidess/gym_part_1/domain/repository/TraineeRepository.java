package com.dbenavidess.gym_part_1.domain.repository;

import com.dbenavidess.gym_part_1.domain.model.Trainee;
import com.dbenavidess.gym_part_1.domain.model.Trainer;

import java.util.List;
import java.util.UUID;

public interface TraineeRepository {
    Trainee createTrainee(Trainee trainee);
    Trainee updateTrainee(Trainee trainee);
    void deleteTrainee(UUID id);
    void deleteByUsername(String username);
    Trainee getByUsername(String username);

    List<Trainer> getTrainers(Trainee trainee);
    List<Trainer> updateTraineeTrainerList(List<String> trainers, Trainee trainee);

    List<Trainer> getNotAssignedTrainers(Trainee trainee);
}
