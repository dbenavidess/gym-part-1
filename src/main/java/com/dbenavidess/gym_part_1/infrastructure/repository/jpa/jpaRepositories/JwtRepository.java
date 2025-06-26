package com.dbenavidess.gym_part_1.infrastructure.repository.jpa.jpaRepositories;

import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.entitites.JwtEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JwtRepository extends JpaRepository<JwtEntity,Integer> {
    Optional<JwtEntity> findByJwt(String jwt);
    Optional<JwtEntity> findByUser_Username(String username);

}
