package com.dbenavidess.gym_part_1.domain.model;


import com.dbenavidess.gym_part_1.domain.repository.UserRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.UUID;

@Getter
public class User implements Serializable {
    private UUID id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private boolean isActive;

    private UserRepository userRepository;

    public User() {
    }

    public User(String firstName, String lastName, boolean isActive) {
        this.id = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = isActive;
        this.username = calculateUsername();
        this.password = calculatePassword();
    }

    public User(UUID id, String firstName, String lastName, String username, String password, boolean isActive) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.isActive = isActive;
    }

    private String calculatePassword(){
        byte[] array = new byte[10];
        new Random().nextBytes(array);

        return new String(array, StandardCharsets.UTF_8);
    }

    private String calculateUsername(){
        int number = userRepository.search(user -> user.username.contains(this.firstName + "." + this.lastName)).size();
        if(number > 0){
            return this.firstName + "." + this.lastName + number;
        }
        return this.firstName + "." + this.lastName;

    }

    @Autowired
    void setUserRepository(UserRepository repository){
        this.userRepository = repository;
    }

}
