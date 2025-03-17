package com.dbenavidess.domain;

import lombok.Getter;
import java.util.Date;

@Getter
public class Trainee {
    private final User user;
    private final String address;
    private final Date dateOfBirth;

    public Trainee(String address, Date dateOfBirth, User user) {
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.user = user;
    }
}
