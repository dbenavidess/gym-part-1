package com.dbenavidess.gym_part_1.infrastructure.repository.InMemory;

import com.dbenavidess.gym_part_1.domain.model.Trainee;
import com.dbenavidess.gym_part_1.domain.model.Trainer;
import com.dbenavidess.gym_part_1.domain.model.Training;
import com.dbenavidess.gym_part_1.domain.model.TrainingType;
import com.dbenavidess.gym_part_1.domain.repository.TrainingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;
@Repository
@ConditionalOnProperty(name = "spring.application.persistence", havingValue = "inmemory")
public class InMemoryTrainingRepository implements TrainingRepository {

    @Autowired
    private Map<UUID, Training> storage;

    @Override
    public Training createTraining(Training training) {
        if (storage.containsKey(training.getId())
                || training.getTrainer() == null
                || training.getTrainee() == null){
            return null;
        }
        storage.put(training.getId(),training);
        return storage.get(training.getId());
    }

    @Override
    public void deleteTraining(UUID id) {
        storage.remove(id);
    }

    @Override
    public Training getTraining(UUID id) {
        return storage.get(id);
    }

    @Override
    public List<Training> getAllTrainings() {
        return storage.values().stream().toList() ;
    }

    @Override
    public List<Training> searchTrainings(Date from, Date to, Trainer trainer, Trainee trainee, TrainingType type) {
        return List.of();
    }

}
