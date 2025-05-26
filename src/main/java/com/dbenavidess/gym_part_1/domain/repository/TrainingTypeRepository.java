package com.dbenavidess.gym_part_1.domain.repository;

import com.dbenavidess.gym_part_1.domain.model.TrainingType;

import java.util.List;
import java.util.UUID;

public interface TrainingTypeRepository {
    TrainingType getById(UUID id);
    TrainingType getByName(String name);
    List<TrainingType> getAll();
}
