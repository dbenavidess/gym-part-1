package com.dbenavidess.gym_part_1.infrastructure.repository.jpa;

import com.dbenavidess.gym_part_1.domain.model.Trainee;
import com.dbenavidess.gym_part_1.domain.model.Trainer;
import com.dbenavidess.gym_part_1.domain.model.Training;
import com.dbenavidess.gym_part_1.domain.model.TrainingType;
import com.dbenavidess.gym_part_1.domain.repository.TrainingRepository;
import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.entitites.TrainingEntity;
import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.jpaRepositories.TrainingEntityJpaRepository;
import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.jpaRepositories.Specifications.TrainingSpecification;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TrainingJpaRepository implements TrainingRepository {

    private final TrainingEntityJpaRepository repository;

    public TrainingJpaRepository(TrainingEntityJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Training createTraining(Training training) {
        if (training.getTrainer() == null || training.getTrainee() == null || training.getType() == null){
            throw new RuntimeException("Invalid Training");
        }
        TrainingEntity trainingEntity = TrainingEntity.of(training);
        return repository.save(trainingEntity).toDomain();
    }

    @Override
    public void deleteTraining(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public Training getTraining(UUID id) {
        Optional<TrainingEntity> opt = repository.findById(id);
        return opt.map(TrainingEntity::toDomain).orElse(null);
    }


    @Override
    public List<Training> searchTrainings(Date from, Date to, Trainer trainer, Trainee trainee, TrainingType type) {
        UUID trainerId = trainer == null? null : trainer.getId();
        UUID traineeId = trainee == null? null : trainee.getId();
        UUID typeId = type == null? null : type.getId();
        return repository.findAll(
                TrainingSpecification.filter(trainerId, from, to, traineeId, typeId)
        ).stream()
                .map(TrainingEntity::toDomain)
                .toList();

    }
}
