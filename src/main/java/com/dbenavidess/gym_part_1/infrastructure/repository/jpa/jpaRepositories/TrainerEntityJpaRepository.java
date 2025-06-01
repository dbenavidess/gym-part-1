package com.dbenavidess.gym_part_1.infrastructure.repository.jpa.jpaRepositories;

import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.entitites.TrainerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TrainerEntityJpaRepository extends JpaRepository<TrainerEntity, UUID>{
    Optional<TrainerEntity> findByUser_Username(String username);



}
