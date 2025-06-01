package com.dbenavidess.gym_part_1.infrastructure.repository.jpa.jpaRepositories;

import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.entitites.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserEntityJpaRepository extends JpaRepository<UserEntity,UUID> {
    List<UserEntity> findByUsernameContaining(String s);
    Optional<UserEntity> findByUsername(String s);
}
