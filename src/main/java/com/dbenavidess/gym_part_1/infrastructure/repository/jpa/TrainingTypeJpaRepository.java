package com.dbenavidess.gym_part_1.infrastructure.repository.jpa;

import com.dbenavidess.gym_part_1.domain.model.TrainingType;
import com.dbenavidess.gym_part_1.domain.repository.TrainingTypeRepository;
import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.entitites.TrainingTypeEntity;
import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.jpaRepositories.TrainingTypeEntityJpaRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TrainingTypeJpaRepository implements TrainingTypeRepository {

    private final TrainingTypeEntityJpaRepository repository;

    public TrainingTypeJpaRepository(TrainingTypeEntityJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public TrainingType getById(UUID id) {
        Optional<TrainingTypeEntity> opt = repository.findById(id);
        return opt.map(TrainingTypeEntity::toDomain).orElse(null);
    }

    @Override
    public TrainingType getByName(String name) {
        Optional<TrainingTypeEntity> opt = repository.findByName(name);
        return opt.map(TrainingTypeEntity::toDomain).orElse(null);
    }

    @Override
    public List<TrainingType> getAll() {
        return repository.findAll().stream()
                .map(TrainingTypeEntity::toDomain)
                .toList();
    }

    @PostConstruct
    private void populateTrainingTypes(){
        String[] types = {"fitness", "yoga", "zumba", "stretching", "resistance"};
        for(String type : types){
            if(repository.findByName(type).isEmpty()){
                repository.save(TrainingTypeEntity.of(new TrainingType(type)));
            }
        }
    }
}
