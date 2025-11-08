package com.dbenavidess.gym_part_1.componentTest.service;

import com.dbenavidess.gym_part_1.domain.model.User;
import com.dbenavidess.gym_part_1.domain.repository.UserRepository;
import com.dbenavidess.gym_part_1.domain.util.PasswordEncryptionProvider;
import com.dbenavidess.gym_part_1.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncryptionProvider passwordEncryptionProvider;

    @Test
    public void loginTest(){
        // Arrange
        User user = new User("Daniel","Benavides",true, repository, passwordEncryptionProvider);
        User createdUser = repository.createUser(user);

        // Act & Assert
        assertTrue(userService.loginMatch(createdUser.getUsername(),user.getPlainPassword()));
        assertFalse(userService.loginMatch(createdUser.getUsername(),"wrong" + createdUser.getPassword()));

        repository.deleteUser(createdUser.getId());

    }

    @Test
    public void changeActiveStatusTest(){
        // Arrange
        User user = new User("Daniel","Benavides",true, repository, passwordEncryptionProvider);
        User createdUser = repository.createUser(user);

        // Act
        userService.changeActiveStatus(createdUser.getUsername());
        User foundUser = repository.searchUsername(user.getUsername());

        //Assert
        assertNotEquals(foundUser.getIsActive(),createdUser.getIsActive());

        repository.deleteUser(createdUser.getId());


    }

    @Test
    public void changePasswordTest(){
        // Arrange
        User user = new User("Daniel","Benavides",true, repository, passwordEncryptionProvider);
        User createdUser = repository.createUser(user);

        // Act
        userService.changePassword(createdUser.getUsername(), createdUser.getPassword(), "NewPassword");
        User foundUser = repository.searchUsername(user.getUsername());
        //Assert
        assertTrue(passwordEncryptionProvider.matches("NewPassword",foundUser.getPassword()));

        repository.deleteUser(createdUser.getId());


    }
}
