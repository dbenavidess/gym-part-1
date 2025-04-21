package com.dbenavidess.gym_part_1.domain.repository;

import com.dbenavidess.gym_part_1.domain.model.Trainee;
import com.dbenavidess.gym_part_1.domain.model.Trainer;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public interface TraineeRepository {
    Trainee createTrainee(Trainee trainee);
    Trainee updateTrainee(Trainee trainee);
    void deleteTrainee(UUID id);
    void deleteByUsername(String username);
    Trainee getTrainee(UUID id);
    Trainee getByUsername(String username);

    List<Trainee> getAllTrainees();
    List<Trainer> getTrainers(Trainee trainee);
    List<Trainer> addTrainerToTrainee(UUID traineeId, UUID trainerId);

    List<Trainer> getNotAssignedTrainers(Trainee trainee);
}
