package com.dbenavidess.gym_part_1.infrastructure.request.trainer;

import jakarta.validation.constraints.NotNull;

public class UpdateTrainerRequest {
    @NotNull
    public String username;
    @NotNull
    public String firstName;
    @NotNull
    public String lastName;
    @NotNull
    public boolean isActive;
    @NotNull
    public String specialization;

    public UpdateTrainerRequest(String username, String firstName, String lastName, boolean isActive, String specialization) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = isActive;
        this.specialization = specialization;
    }
}
