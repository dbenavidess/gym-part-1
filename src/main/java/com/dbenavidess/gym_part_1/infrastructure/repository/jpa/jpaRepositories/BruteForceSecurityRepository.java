package com.dbenavidess.gym_part_1.infrastructure.repository.jpa.jpaRepositories;

import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.entitites.BruteForceSecurityEntity;
import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.entitites.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BruteForceSecurityRepository extends JpaRepository<BruteForceSecurityEntity,Integer> {
    BruteForceSecurityEntity findByUser(UserEntity userEntity);
}
