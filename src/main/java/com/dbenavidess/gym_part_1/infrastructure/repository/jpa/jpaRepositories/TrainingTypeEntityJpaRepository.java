package com.dbenavidess.gym_part_1.infrastructure.repository.jpa.jpaRepositories;

import com.dbenavidess.gym_part_1.domain.model.TrainingType;
import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.entitites.TrainingTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TrainingTypeEntityJpaRepository extends JpaRepository<TrainingTypeEntity,UUID> {
    Optional<TrainingTypeEntity> findByName(String name);
}
