package com.dbenavidess.gym_part_1.infrastructure.repository.jpa.jpaRepositories;

import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.entitites.TraineeEntity;
import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.entitites.TrainerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TraineeEntityJpaRepository extends JpaRepository<TraineeEntity, UUID>{
    Optional<TraineeEntity> findByUser_Username(String username);
    @Query("""
        SELECT t 
        FROM TrainerEntity t 
        WHERE t.user.isActive = true 
        AND t.id NOT IN (
            SELECT tt.id 
            FROM TrainerEntity tt 
            JOIN tt.trainees tr 
            WHERE tr.id = :traineeId
        )
    """)
    List<TrainerEntity> findAllNotAssignedToTrainee(@Param("traineeId") UUID traineeId);

}
