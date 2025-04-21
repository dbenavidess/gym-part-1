package com.dbenavidess.gym_part_1.infrastructure.repository.InMemory;

import com.dbenavidess.gym_part_1.domain.model.User;
import com.dbenavidess.gym_part_1.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

@Repository
@ConditionalOnProperty(name = "spring.application.persistence", havingValue = "inmemory")
public class InMemoryUserRepository implements UserRepository {

    @Autowired
    public Map<UUID, User> storage;

    @Override
    public User createUser(User user) {
        if (storage.containsKey(user.getId())){
            return null;
        }
        storage.put(user.getId(),user);
        return storage.get(user.getId());
    }

    @Override
    public User updateUser(User user) {
        storage.put(user.getId(),user);
        return storage.get(user.getId());
    }

    @Override
    public void deleteUser(UUID id) {
        storage.remove(id);
    }

    @Override
    public User getUser(UUID id) {
        return storage.get(id);
    }

    @Override
    public List<User> searchUsernameLike(String username) {
        return storage.values().stream()
                .filter(user -> user.getUsername().contains(username))
                .toList();
    }

    @Override
    public User searchUsername(String s) {
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return storage.values().stream().toList();
    }
}
