package com.dbenavidess.gym_part_1.infrastructure.repository.InMemory;

import com.dbenavidess.gym_part_1.domain.model.Training;
import com.dbenavidess.gym_part_1.domain.repository.TrainingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;
@Repository
public class InMemoryTrainingRepository implements TrainingRepository {
    @Autowired
    private Map<UUID, Training> storage;

    @Override
    public Training createTraining(Training training) {
        if (storage.containsKey(training.getId())){
            return null;
        }
        storage.put(training.getId(),training);
        return storage.get(training.getId());
    }

    @Override
    public Training updateTraining(Training training) {
        return storage.put(training.getId(),training);
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
    public List<Training> search(Predicate<Training> p) {
        return storage.values().stream().filter(p).toList();
    }

    @Override
    public List<Training> getAllTrainings() {
        return storage.values().stream().toList() ;
    }
}
