package com.dbenavidess.gym_part_1.infrastructure.repository.jpa;

import com.dbenavidess.gym_part_1.domain.model.User;
import com.dbenavidess.gym_part_1.domain.repository.UserRepository;
import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.entitites.UserEntity;
import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.jpaRepositories.UserEntityJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserJpaRepository implements UserRepository {

    @Autowired
    UserEntityJpaRepository repository;

    @Override
    public User createUser(User user) {
        UserEntity userEntity = UserEntity.of(user);
        return repository.save(userEntity).toDomain();
    }

    @Override
    public User updateUser(User user) {
        UserEntity userEntity = repository.findById(user.getId()).orElseThrow();
        userEntity.setActive(user.getIsActive());
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setPassword(user.getPassword());
        repository.save(userEntity);
        return repository.save(userEntity).toDomain();
    }

    @Override
    public void deleteUser(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public User getUser(UUID id) {
        Optional<UserEntity> opt = repository.findById(id);
        return opt.map(UserEntity::toDomain).orElse(null);
    }

    @Override
    public List<User> getAllUsers() {
        return repository.findAll().stream().map(UserEntity::toDomain).toList();
    }

    @Override
    public List<User> searchUsernameLike(String s) {
        return repository.findByUsernameContaining(s).stream().map(UserEntity::toDomain).toList();
    }

    @Override
    public User searchUsername(String s) {
        Optional<UserEntity> opt = repository.findByUsername(s);
        return opt.map(UserEntity::toDomain).orElse(null);
    }
}
