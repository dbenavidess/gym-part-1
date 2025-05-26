package com.dbenavidess.gym_part_1.domain.repository;

import com.dbenavidess.gym_part_1.domain.model.User;

import java.util.List;
import java.util.UUID;

public interface UserRepository {
    User createUser(User user);
    User updateUser(User user);
    void deleteUser(UUID id);
    User getUser(UUID id);
    User getUserByUsername(String username);

    List<User> searchUsernameLike(String s);
    User searchUsername(String s);
}
