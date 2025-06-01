package com.dbenavidess.gym_part_1.application.service;

import com.dbenavidess.gym_part_1.domain.model.User;
import com.dbenavidess.gym_part_1.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService {

    @Autowired
    UserRepository repository;

    public boolean login(String username, String password){
        User user = repository.searchUsername(username);
        return username.equals(user.getUsername()) && password.equals(user.getPassword());
    }

    public boolean changeActiveStatus(String username){
        User user = repository.getUserByUsername(username);
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

    public boolean changePassword(String username, String oldPassword, String newPassword){
        if (!login(username,oldPassword)){
            throw new IllegalArgumentException();
        }

        User user = repository.getUserByUsername(username);
        User updatedUser = repository.updateUser(new User(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                newPassword,
                user.getIsActive()
        ));
        return Objects.equals(updatedUser.getPassword(), newPassword);
    }






}
