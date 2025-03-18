package com.dbenavidess.gym_part_1.domain.model;

import lombok.Getter;
import java.util.Date;
import java.util.UUID;

@Getter
public class Trainee {
    private final UUID id;
    private final User user;
    private final String address;
    private final Date dateOfBirth;

    public Trainee(String address, Date dateOfBirth, User user) {
        this.id = UUID.randomUUID();
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.user = user;
    }
}
