package com.dbenavidess.gym_part_1.infrastructure.repository.jpa;

import com.dbenavidess.gym_part_1.domain.model.Trainee;
import com.dbenavidess.gym_part_1.domain.model.Trainer;
import com.dbenavidess.gym_part_1.domain.repository.TrainerRepository;
import com.dbenavidess.gym_part_1.domain.repository.UserRepository;
import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.entitites.TraineeEntity;
import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.entitites.TrainerEntity;
import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.entitites.TrainingTypeEntity;
import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.jpaRepositories.TraineeEntityJpaRepository;
import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.jpaRepositories.TrainerEntityJpaRepository;
import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.jpaRepositories.TrainingTypeEntityJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TrainerJpaRepository implements TrainerRepository {

    private final TrainerEntityJpaRepository repository;
    private final UserRepository userRepository;
    private final TrainingTypeEntityJpaRepository trainingTypeRepository;

    public TrainerJpaRepository(TrainerEntityJpaRepository repository, UserRepository userRepository, TrainingTypeEntityJpaRepository trainingTypeRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.trainingTypeRepository = trainingTypeRepository;
    }

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
        TrainerEntity trainerEntity = repository.findByUser_Username(trainer.getUser().getUsername()).orElseThrow();
        trainerEntity.setSpecialization(TrainingTypeEntity.of(trainer.getSpecialization()));
        userRepository.updateUser(trainer.getUser());

        return repository.save(trainerEntity).toDomain();
    }

    @Override
    public void deleteTrainer(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public List<Trainee> getTrainees(Trainer trainer) {

        return repository.findById(trainer.getId()).orElseThrow().getTrainees()
                .stream()
                .map(TraineeEntity::toDomain)
                .toList();
    }

    @Override
    @Transactional
    public Trainer getByUsername(String username) {
        Optional<TrainerEntity> opt = repository.findByUser_Username(username);
        return opt.map(TrainerEntity::toDomain).orElse(null);
    }

    @Override
    public List<Trainer> getAllTrainers() {
        return repository.findAll().stream().map(TrainerEntity::toDomain).toList();
    }

}
