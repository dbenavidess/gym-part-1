package com.dbenavidess.gym_part_1.domain.repository;

import com.dbenavidess.gym_part_1.domain.model.Training;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public interface TrainingRepository {
    Training createTraining(Training training);
    Training updateTraining(Training training);
    void deleteTraining(UUID id);
    Training getTraining(UUID id);

    List<Training> search(Predicate<Training> p);

    List<Training> getAllTrainings();
}
