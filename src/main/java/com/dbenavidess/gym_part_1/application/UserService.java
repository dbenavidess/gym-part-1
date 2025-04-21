package com.dbenavidess.gym_part_1.application;

import com.dbenavidess.gym_part_1.domain.model.User;
import com.dbenavidess.gym_part_1.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    UserRepository repository;

    public boolean login(String username, String password){
        User user = repository.searchUsername(username);
        return username.equals(user.getUsername()) && password.equals(user.getPassword());
    }

    public boolean changeActiveStatus(UUID id){
        User user = repository.getUser(id);
        User updatedUser = new User(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getPassword(),
                !user.getIsActive()
                );
        return Objects.equals(repository.updateUser(updatedUser).getIsActive(),!user.getIsActive());
    }

    public boolean changePassword(UUID id, String newPassword){
        User user = repository.getUser(id);
        User updatedUser = new User(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                newPassword,
                user.getIsActive()
        );
        repository.updateUser(updatedUser);
        return Objects.equals(repository.updateUser(updatedUser).getPassword(), newPassword);
    }






}
