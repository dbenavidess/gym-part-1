package com.dbenavidess.gym_part_1.domain.repository;

import com.dbenavidess.gym_part_1.domain.model.Trainee;
import com.dbenavidess.gym_part_1.domain.model.Trainer;
import com.dbenavidess.gym_part_1.domain.model.Training;
import com.dbenavidess.gym_part_1.domain.model.TrainingType;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

public interface TrainingRepository {
    Training createTraining(Training training);
    void deleteTraining(UUID id);
    Training getTraining(UUID id);

    List<Training> searchTrainings(Date from, Date to, Trainer trainer, Trainee trainee, TrainingType type);
}
