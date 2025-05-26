package com.dbenavidess.gym_part_1.infrastructure.request.Trainee;

import jakarta.validation.constraints.NotNull;

import java.sql.Date;

public class UpdateTraineeRequest {

    @NotNull
    public String username;
    @NotNull
    public String firstName;
    @NotNull
    public String lastName;

    public Date dateOfBirth;

    public String address;

    @NotNull
    public boolean isActive;

    public UpdateTraineeRequest(String username, String firstName, String lastName, Date dateOfBirth, String address, boolean isActive) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.isActive = isActive;
    }
}
