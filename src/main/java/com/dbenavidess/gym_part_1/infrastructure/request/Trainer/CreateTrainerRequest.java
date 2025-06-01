package com.dbenavidess.gym_part_1.infrastructure.request.Trainer;

import jakarta.validation.constraints.NotNull;

public class CreateTrainerRequest {
    @NotNull
    public String firstName;
    @NotNull
    public String lastName;
    @NotNull
    public String specialization;

    public CreateTrainerRequest(String firstName, String lastName, String specialization) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialization = specialization;
    }
}
