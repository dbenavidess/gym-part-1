package com.dbenavidess.gym_part_1.infrastructure.repository.InMemory;

import com.dbenavidess.gym_part_1.domain.model.Trainer;
import com.dbenavidess.gym_part_1.domain.repository.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

@Repository
public class InMemoryTrainerRepository implements TrainerRepository {

    @Autowired
    private Map<UUID,Trainer> storage;

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
    public void deleteTrainer(UUID id) {
        storage.remove(id);
    }

    @Override
    public Trainer getTrainer(UUID id) {
        return storage.get(id);
    }

    @Override
    public List<Trainer> search(Predicate<Trainer> p){
        return storage.values().stream()
                .filter(p)
                .toList();
    }

    @Override
    public List<Trainer> getAllTrainers() {
        return storage.values().stream().toList();
    }
}
