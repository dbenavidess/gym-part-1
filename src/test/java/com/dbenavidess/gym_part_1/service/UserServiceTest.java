package com.dbenavidess.gym_part_1.service;

import com.dbenavidess.gym_part_1.application.service.UserService;
import com.dbenavidess.gym_part_1.domain.model.User;
import com.dbenavidess.gym_part_1.domain.repository.UserRepository;
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



    @Test
    public void loginTest(){
        // Arrange
        User user = new User("Daniel","Benavides",true, repository);
        User createdUser = repository.createUser(user);

        // Act & Assert
        assertTrue(userService.login(createdUser.getUsername(),createdUser.getPassword()));
        assertFalse(userService.login(createdUser.getUsername(),"wrong" + createdUser.getPassword()));

        repository.deleteUser(createdUser.getId());

    }

    @Test
    public void changeActiveStatusTest(){
        // Arrange
        User user = new User("Daniel","Benavides",true, repository);
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
        User user = new User("Daniel","Benavides",true, repository);
        User createdUser = repository.createUser(user);

        // Act
        userService.changePassword(createdUser.getUsername(), createdUser.getPassword(), "NewPassword");
        User foundUser = repository.searchUsername(user.getUsername());
        //Assert
        assertEquals("NewPassword",foundUser.getPassword());

        repository.deleteUser(createdUser.getId());


    }
}
