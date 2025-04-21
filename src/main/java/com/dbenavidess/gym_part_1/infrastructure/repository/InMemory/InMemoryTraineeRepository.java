package com.dbenavidess.gym_part_1.infrastructure.repository.InMemory;

import com.dbenavidess.gym_part_1.domain.model.Trainee;
import com.dbenavidess.gym_part_1.domain.model.Trainer;
import com.dbenavidess.gym_part_1.domain.model.User;
import com.dbenavidess.gym_part_1.domain.repository.TraineeRepository;
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
public class InMemoryTraineeRepository implements TraineeRepository {

    @Autowired
    private Map<UUID, Trainee> storage;

    @Autowired
    private Map<UUID, User> userStorage;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Trainee createTrainee(Trainee trainee) {
        if (storage.containsKey(trainee.getId()) || trainee.getUser() == null){
            return null;
        }
        userRepository.createUser(trainee.getUser());
        storage.put(trainee.getId(),trainee);
        return storage.get(trainee.getId());
    }

    @Override
    public Trainee updateTrainee(Trainee trainee) {
        userRepository.updateUser(trainee.getUser());
        storage.put(trainee.getId(),trainee);
        return storage.get(trainee.getId());
    }

    @Override
    public void deleteTrainee(UUID id) {
        storage.remove(id);
    }

    @Override
    public void deleteByUsername(String username) {

    }

    @Override
    public Trainee getTrainee(UUID id) {
        return storage.get(id);
    }

    @Override
    public Trainee getByUsername(String username) {
        return null;
    }

    @Override
    public List<Trainee> getAllTrainees() {
        return storage.values().stream().toList();
    }

    @Override
    public List<Trainer> getTrainers(Trainee trainee) {
        return List.of();
    }

    @Override
    public List<Trainer> addTrainerToTrainee(UUID traineeId, UUID trainerId) {
        return List.of();
    }

    @Override
    public List<Trainer> getNotAssignedTrainers(Trainee trainee) {
        return List.of();
    }
}
