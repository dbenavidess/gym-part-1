package com.dbenavidess.gym_part_1.infrastructure.repository.jpa;

import com.dbenavidess.gym_part_1.domain.model.Trainee;
import com.dbenavidess.gym_part_1.domain.model.Trainer;
import com.dbenavidess.gym_part_1.domain.repository.TrainerRepository;
import com.dbenavidess.gym_part_1.domain.repository.TrainingTypeRepository;
import com.dbenavidess.gym_part_1.domain.repository.UserRepository;
import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.entitites.TraineeEntity;
import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.entitites.TrainerEntity;
import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.entitites.UserEntity;
import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.jpaRepositories.TraineeEntityJpaRepository;
import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.jpaRepositories.TrainerEntityJpaRepository;
import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.jpaRepositories.TrainingTypeEntityJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TrainerJpaRepository implements TrainerRepository {

    @Autowired
    TrainerEntityJpaRepository repository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TrainingTypeEntityJpaRepository trainingTypeRepository;

    @Autowired
    TraineeEntityJpaRepository traineeEntityJpaRepository;

    @Transactional
    @Override
    public Trainer createTrainer(Trainer trainer) {
        if(trainer.getUser() == null){
            throw new RuntimeException("Invalid Trainer");
        }
        TrainerEntity trainerEntity = TrainerEntity.of(trainer);
        userRepository.createUser(trainerEntity.getUser().toDomain());

        return repository.save(trainerEntity).toDomain();
    }

    @Transactional
    @Override
    public Trainer updateTrainer(Trainer trainer) {
        TrainerEntity trainerEntity = repository.findById(trainer.getId()).get();
        trainerEntity.setSpecialization(trainingTypeRepository.findById(trainer.getSpecialization().getId()).get());

        userRepository.updateUser(trainer.getUser());

        return repository.save(trainerEntity).toDomain();
    }

    @Override
    public void deleteTrainer(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public Trainer getTrainer(UUID id) {
        Optional<TrainerEntity> opt = repository.findById(id);
        return opt.map(TrainerEntity::toDomain).orElse(null);
    }

    @Override
    public Trainer getByUsername(String username) {
        Optional<TrainerEntity> opt = repository.findByUser_Username(username);
        return opt.map(TrainerEntity::toDomain).orElse(null);
    }

    @Override
    public List<Trainer> getAllTrainers() {
        return repository.findAll().stream().map(TrainerEntity::toDomain).toList();
    }

    @Override
    @Transactional
    public List<Trainee> addTraineeToTrainer(UUID traineeId, UUID trainerId) {
        TrainerEntity trainer = repository.findById(traineeId).orElseThrow();
        TraineeEntity trainee = traineeEntityJpaRepository.findById(trainerId).orElseThrow();

        trainee.getTrainers().add(trainer);
        trainer.getTrainees().add(trainee);

        repository.save(trainer);
        traineeEntityJpaRepository.save(trainee);

        return trainer.getTrainees()
                .stream()
                .map(TraineeEntity::toDomain)
                .toList();
    }
}
