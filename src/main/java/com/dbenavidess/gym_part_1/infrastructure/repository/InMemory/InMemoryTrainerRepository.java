package com.dbenavidess.gym_part_1.infrastructure.repository.InMemory;

import com.dbenavidess.gym_part_1.domain.model.Trainer;
import com.dbenavidess.gym_part_1.domain.repository.TrainerRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class InMemoryTrainerRepository implements TrainerRepository {

    private final Map<UUID,Trainer> storage = new HashMap<>();

    @Override
    public Trainer createTrainer(Trainer trainer) {
        if (storage.containsKey(trainer.getId())){
            return null;
        }
        storage.put(trainer.getId(),trainer);
        return storage.get(trainer.getId());
    }

    @Override
    public Trainer updateTrainer(Trainer trainer) {
        storage.put(trainer.getId(),trainer);
        return storage.get(trainer.getId());
    }

    @Override
    public void deleteTrainer(Trainer trainer) {
        storage.remove(trainer.getId());
    }

    @Override
    public Trainer getTrainer(UUID id) {
        return storage.get(id);
    }

    @Override
    public List<Trainer> getAllTrainers() {
        return storage.values().stream().toList();
    }
}
