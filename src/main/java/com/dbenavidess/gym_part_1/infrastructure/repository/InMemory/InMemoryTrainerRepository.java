package com.dbenavidess.gym_part_1.infrastructure.repository.InMemory;

import com.dbenavidess.gym_part_1.domain.model.Trainee;
import com.dbenavidess.gym_part_1.domain.model.Trainer;
import com.dbenavidess.gym_part_1.domain.repository.TrainerRepository;
import com.dbenavidess.gym_part_1.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

@Repository
@ConditionalOnProperty(name = "spring.application.persistence", havingValue = "inmemory")
public class InMemoryTrainerRepository implements TrainerRepository {

    @Autowired
    private Map<UUID,Trainer> storage;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Trainer createTrainer(Trainer trainer) {
        if (storage.containsKey(trainer.getId())
        || trainer.getUser() == null
        || trainer.getSpecialization() == null){
            return null;
        }
        userRepository.createUser(trainer.getUser());
        storage.put(trainer.getId(),trainer);
        return storage.get(trainer.getId());
    }

    @Override
    public Trainer updateTrainer(Trainer trainer) {
        userRepository.updateUser(trainer.getUser());
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
    public Trainer getByUsername(String username) {
        return null;
    }

    @Override
    public List<Trainer> getAllTrainers() {
        return storage.values().stream().toList();
    }

    @Override
    public List<Trainee> addTraineeToTrainer(UUID traineeId, UUID trainerId) {
        return List.of();
    }
}
