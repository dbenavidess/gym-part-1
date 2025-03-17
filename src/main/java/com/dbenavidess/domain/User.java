package com.dbenavidess.domain;

import lombok.Getter;
import java.nio.charset.StandardCharsets;
import java.util.Random;

@Getter
public abstract class User {
    private final long id;
    private final String firstName;
    private final String lastName;
    private final String username;
    private final String password;
    private final boolean isActive;

    public User(long id, String firstName, String lastName, boolean isActive) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = isActive;
        this.username = firstName + "." + lastName;
        this.password = calculatePassword();
    }

    public User(long id, String firstName, String lastName, String username, String password, boolean isActive) {
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

}
