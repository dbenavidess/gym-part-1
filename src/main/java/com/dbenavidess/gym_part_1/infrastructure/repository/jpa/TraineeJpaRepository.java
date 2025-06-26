package com.dbenavidess.gym_part_1.infrastructure.repository.jpa;

import com.dbenavidess.gym_part_1.domain.model.Trainee;
import com.dbenavidess.gym_part_1.domain.model.Trainer;
import com.dbenavidess.gym_part_1.domain.repository.TraineeRepository;
import com.dbenavidess.gym_part_1.domain.repository.UserRepository;
import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.entitites.TraineeEntity;
import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.entitites.TrainerEntity;
import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.jpaRepositories.TraineeEntityJpaRepository;

import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.jpaRepositories.TrainerEntityJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class TraineeJpaRepository implements TraineeRepository {

    private final TraineeEntityJpaRepository repository;
    private final TrainerEntityJpaRepository trainerEntityJpaRepository;
    private final UserRepository userRepository;

    public TraineeJpaRepository(TraineeEntityJpaRepository repository, TrainerEntityJpaRepository trainerEntityJpaRepository, UserRepository userRepository) {
        this.repository = repository;
        this.trainerEntityJpaRepository = trainerEntityJpaRepository;
        this.userRepository = userRepository;
    }

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
        TraineeEntity traineeEntity = repository.findByUser_Username(trainee.getUser().getUsername()).orElseThrow();
        if (trainee.getAddress() != null && !trainee.getAddress().isEmpty()){
            traineeEntity.setAddress(trainee.getAddress());
        }
        if (trainee.getDateOfBirth() != null){
            traineeEntity.setDateOfBirth(trainee.getDateOfBirth());
        }

        userRepository.updateUser(trainee.getUser());

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
    @Transactional
    public Trainee getByUsername(String username) {
        Optional<TraineeEntity> opt = repository.findByUser_Username(username);
        return opt.map(TraineeEntity::toDomain).orElse(null);
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
    public List<Trainer> updateTraineeTrainerList(List<String> trainers, Trainee trainee) {
        TraineeEntity entity = repository.findById(trainee.getId()).orElseThrow();
        entity.setTrainers(trainers.stream()
                .map(trainerUsername -> trainerEntityJpaRepository.findByUser_Username(trainerUsername).orElseThrow())
                .collect(Collectors.toList()));
        entity = repository.save(entity);

        return entity.getTrainers().stream()
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
