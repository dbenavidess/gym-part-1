package com.dbenavidess.gym_part_1.domain.model;

import com.dbenavidess.gym_part_1.domain.repository.UserRepository;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@ToString
@EqualsAndHashCode
public class User implements Serializable {
    @Getter
    private UUID id;
    @Getter
    private String firstName;
    @Getter
    private String lastName;
    @Getter
    private String username;
    @Getter
    private String password;
    private boolean isActive;


    public User() {
    }

    public User(String firstName, String lastName, boolean isActive, UserRepository repository) {
        this.id = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = isActive;
        this.username = calculateUsername(repository);
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

    private String calculateUsername(UserRepository userRepository){
        List<User> list = userRepository.search(user -> user.username.contains(this.firstName + "." + this.lastName));
        System.out.println(list.toString());
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
