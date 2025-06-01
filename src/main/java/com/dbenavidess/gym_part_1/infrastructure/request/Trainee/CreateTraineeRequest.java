package com.dbenavidess.gym_part_1.infrastructure.request.Trainee;

import jakarta.validation.constraints.NotNull;

import java.sql.Date;

public class CreateTraineeRequest {

    @NotNull
    public String firstName;
    @NotNull
    public String lastName;

    public Date dateOfBirth;

    public String address;

    public CreateTraineeRequest(String firstName, String lastName, Date dateOfBirth, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }
}
