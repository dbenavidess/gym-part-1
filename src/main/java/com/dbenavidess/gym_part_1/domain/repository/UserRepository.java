package com.dbenavidess.gym_part_1.domain.repository;

import com.dbenavidess.gym_part_1.domain.model.User;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public interface UserRepository {
    User createUser(User user);
    User updateUser(User user);
    void deleteUser(UUID id);
    User getUser(UUID id);

    List<User> search(Predicate<User> p);

    List<User> getAllUsers();
}
