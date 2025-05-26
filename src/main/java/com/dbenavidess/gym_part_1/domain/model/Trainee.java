package com.dbenavidess.gym_part_1.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import java.sql.Date;
import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode
public class Trainee {
    private UUID id;
    private User user;
    private String address;
    private Date dateOfBirth;

    public Trainee() {
    }

    public Trainee(String address, Date dateOfBirth, User user) {
        this.id = UUID.randomUUID();
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.user = user;
    }

    public Trainee(UUID id, String address, Date dateOfBirth, User user) {
        this.id = id;
        this.user = user;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
    }
}