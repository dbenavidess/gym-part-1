package com.dbenavidess.gym_part_1.domain.repository;

import com.dbenavidess.gym_part_1.domain.model.Trainee;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public interface TraineeRepository {
    Trainee createTrainee(Trainee trainee);
    Trainee updateTrainee(Trainee trainee);
    void deleteTrainee(UUID id);
    Trainee getTrainee(UUID id);

    List<Trainee> search(Predicate<Trainee> p);

    List<Trainee> getAllTrainees();
}
