package com.dbenavidess.gym_part_1.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode
public class Trainee implements Serializable {
    private UUID id;
    private User user;
    private String address;
    private LocalDate dateOfBirth;

    public Trainee() {
    }

    public Trainee(String address, LocalDate dateOfBirth, User user) {
        this.id = UUID.randomUUID();
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.user = user;
    }

    public Trainee(UUID id, User user, String address, LocalDate dateOfBirth) {
        this.id = id;
        this.user = user;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
    }
}
