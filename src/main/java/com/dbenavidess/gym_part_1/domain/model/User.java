package com.dbenavidess.gym_part_1.domain.model;

import com.dbenavidess.gym_part_1.domain.repository.UserRepository;
import com.dbenavidess.gym_part_1.domain.util.PasswordEncryptionProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import java.util.List;
import java.util.UUID;
import java.security.SecureRandom;

@ToString
@EqualsAndHashCode
public class User {
    @Getter
    @JsonIgnore
    private UUID id;
    @Getter
    private String firstName;
    @Getter
    private String lastName;
    @Getter
    private String username;
    @Getter
    private String password;
    @Getter
    private String plainPassword;

    private boolean isActive;

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom secureRandom = new SecureRandom();


    public User() {
    }

    public User(String firstName, String lastName, boolean isActive, UserRepository repository, PasswordEncryptionProvider passwordEncryptionProvider) {
        this.id = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = isActive;
        this.username = calculateUsername(repository);
        this.password = passwordEncryptionProvider.encode(calculatePassword());
    }

    public User(String username, String firstName, String lastName, boolean isActive) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = isActive;
        this.username = username;
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
        StringBuilder password = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            password.append(ALPHABET.charAt(secureRandom.nextInt(ALPHABET.length())));
        }
        this.plainPassword = password.toString();
        return password.toString();
    }

    private String calculateUsername(UserRepository userRepository){
        List<User> list = userRepository.searchUsernameLike(this.firstName + "." + this.lastName);
        int number = list.size();
        if(number > 0){
            return this.firstName + "." + this.lastName + number;
        }
        return this.firstName + "." + this.lastName;

    }

    public boolean getIsActive() {
        return isActive;
    }
}
