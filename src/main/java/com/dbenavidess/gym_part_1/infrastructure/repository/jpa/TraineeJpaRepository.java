package com.dbenavidess.gym_part_1.infrastructure.repository.jpa;

import com.dbenavidess.gym_part_1.domain.model.Trainee;
import com.dbenavidess.gym_part_1.domain.model.Trainer;
import com.dbenavidess.gym_part_1.domain.repository.TraineeRepository;
import com.dbenavidess.gym_part_1.domain.repository.UserRepository;
import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.entitites.TraineeEntity;
import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.entitites.TrainerEntity;
import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.jpaRepositories.TraineeEntityJpaRepository;

import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.jpaRepositories.TrainerEntityJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TraineeJpaRepository implements TraineeRepository {

    @Autowired
    TraineeEntityJpaRepository repository;

    @Autowired
    TrainerEntityJpaRepository trainerEntityJpaRepository;

    @Autowired
    UserRepository userRepository;

    @Transactional()
    @Override
    public Trainee createTrainee(Trainee trainee) {
        if(trainee.getUser() == null){
            throw new RuntimeException("Invalid Trainee");
        }
        TraineeEntity traineeEntity = TraineeEntity.of(trainee);
        userRepository.createUser(traineeEntity.getUser().toDomain());

        return repository.save(traineeEntity).toDomain();
    }

    @Transactional()
    @Override
    public Trainee updateTrainee(Trainee trainee) {
        TraineeEntity traineeEntity = repository.findById(trainee.getId()).orElseThrow();
        traineeEntity.setAddress(trainee.getAddress());
        traineeEntity.setDateOfBirth(trainee.getDateOfBirth());

        userRepository.updateUser(traineeEntity.getUser().toDomain());

        return repository.save(traineeEntity).toDomain();
    }

    @Override
    public void deleteTrainee(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public void deleteByUsername(String username) {
        repository.delete(repository.findByUser_Username(username).orElseThrow());
    }

    @Override
    public Trainee getTrainee(UUID id) {
        Optional<TraineeEntity> opt = repository.findById(id);
        return opt.map(TraineeEntity::toDomain).orElse(null);
    }

    @Override
    public Trainee getByUsername(String username) {
        Optional<TraineeEntity> opt = repository.findByUser_Username(username);
        return opt.map(TraineeEntity::toDomain).orElse(null);
    }

    @Override
    public List<Trainee> getAllTrainees() {
        return repository.findAll()
                .stream()
                .map(TraineeEntity::toDomain)
                .toList();
    }

    @Override
    public List<Trainer> getTrainers(Trainee trainee) {

        return repository.findById(trainee.getId()).orElseThrow().getTrainers()
                .stream()
                .map(TrainerEntity::toDomain)
                .toList();
    }

    @Override
    @Transactional
    public List<Trainer> addTrainerToTrainee(UUID traineeId, UUID trainerId) {
        TraineeEntity trainee = repository.findById(traineeId).orElseThrow();
        TrainerEntity trainer = trainerEntityJpaRepository.findById(trainerId).orElseThrow();

        trainee.getTrainers().add(trainer);
        trainer.getTrainees().add(trainee);

        repository.save(trainee);
        trainerEntityJpaRepository.save(trainer);

        return trainee.getTrainers()
                .stream()
                .map(TrainerEntity::toDomain)
                .toList();
    }

    @Override
    public List<Trainer> getNotAssignedTrainers(Trainee trainee) {

        return repository.findAllNotAssignedToTrainee(trainee.getId())
                .stream()
                .map(TrainerEntity::toDomain)
                .toList();
    }
}
