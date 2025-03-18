package com.dbenavidess.gym_part_1.domain.model;

import lombok.Getter;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.UUID;

@Getter
public class User {
    private final UUID id;
    private final String firstName;
    private final String lastName;
    private final String username;
    private final String password;
    private final boolean isActive;

    public User(String firstName, String lastName, boolean isActive) {
        this.id = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = isActive;
        this.username = firstName + "." + lastName;
        this.password = calculatePassword();
    }

    public User(String firstName, String lastName, String username, String password, boolean isActive) {
        this.id = UUID.randomUUID();
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

}
