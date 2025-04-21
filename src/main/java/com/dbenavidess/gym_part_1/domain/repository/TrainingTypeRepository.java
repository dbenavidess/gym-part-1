package com.dbenavidess.gym_part_1.domain.repository;

import com.dbenavidess.gym_part_1.domain.model.TrainingType;

import java.util.UUID;

public interface TrainingTypeRepository {
    TrainingType getById(UUID id);
    TrainingType getByName(String name);
    TrainingType createTrainingType(TrainingType type);
}
