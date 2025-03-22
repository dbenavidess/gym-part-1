package com.dbenavidess.gym_part_1.infrastructure.repository.InMemory;

import com.dbenavidess.gym_part_1.domain.model.Trainee;
import com.dbenavidess.gym_part_1.domain.repository.TraineeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;
@Repository
public class InMemoryTraineeRepository implements TraineeRepository {

    @Autowired
    private Map<UUID, Trainee> storage;

    @Override
    public Trainee createTrainee(Trainee trainee) {
        if (storage.containsKey(trainee.getId())){
            return null;
        }
        storage.put(trainee.getId(),trainee);
        return storage.get(trainee.getId());
    }

    @Override
    public Trainee updateTrainee(Trainee trainee) {
        return storage.replace(trainee.getId(),trainee);
    }

    @Override
    public void deleteTrainee(UUID id) {
        storage.remove(id);
    }

    @Override
    public Trainee getTrainee(UUID id) {
        return storage.get(id);
    }

    @Override
    public List<Trainee> search(Predicate<Trainee> p) {
        return List.of();
    }

    @Override
    public List<Trainee> getAllTrainees() {
        return storage.values().stream().toList();
    }
}
