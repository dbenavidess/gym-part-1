package com.dbenavidess.gym_part_1.config.security.service;

import com.dbenavidess.gym_part_1.domain.model.User;
import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.jpaRepositories.BruteForceSecurityRepository;
import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.entitites.BruteForceSecurityEntity;
import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.entitites.UserEntity;
import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.jpaRepositories.UserEntityJpaRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Optional;

@Service
public class BruteForceSecurityService {

    private final BruteForceSecurityRepository repository;
    private final UserEntityJpaRepository userEntityJpaRepository;

    public BruteForceSecurityService(BruteForceSecurityRepository repository,
                                     UserEntityJpaRepository userEntityJpaRepository) {
        this.repository = repository;
        this.userEntityJpaRepository = userEntityJpaRepository;
    }

    public BruteForceSecurityEntity createBruteForceSecurity(String username){
        UserEntity userEntity = userEntityJpaRepository.findByUsername(username).orElseThrow();
        return repository.save(new BruteForceSecurityEntity(userEntity));
    }

    public void addFailedAttempt(User user){
        UserEntity userEntity = userEntityJpaRepository.findById(user.getId()).orElseThrow();
        BruteForceSecurityEntity entity = repository.findByUser(userEntity);
        if(entity == null){
            entity = repository.save(new BruteForceSecurityEntity(userEntity));
        }
        entity.setFailedAttemptsCounter(entity.getFailedAttemptsCounter() + 1);
        if (entity.getFailedAttemptsCounter() >= 3){
            entity.setUnlockDate(new Date(System.currentTimeMillis() + 1000 * 60 * 5));
        }
        repository.save(entity);
    }

    public BruteForceSecurityEntity clearFailedAttempts(BruteForceSecurityEntity entity){
        entity.setUnlockDate(null);
        return repository.save(entity);
    }

    public BruteForceSecurityEntity getByUsername(String username){
        Optional<UserEntity> userEntity = userEntityJpaRepository.findByUsername(username);
        return userEntity.map(repository::findByUser).orElse(null);
    }


}
