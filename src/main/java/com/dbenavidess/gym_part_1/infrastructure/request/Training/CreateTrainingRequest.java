package com.dbenavidess.gym_part_1.infrastructure.request.Training;

import jakarta.validation.constraints.NotNull;

import java.sql.Date;

public class CreateTrainingRequest {
    @NotNull()
    public String trainerUsername;

    @NotNull()
    public String traineeUsername;

    @NotNull()
    public String name;

    @NotNull()
    public String trainingTypeName;

    @NotNull()
    public Date date;

    @NotNull()
    public Integer duration;

    public CreateTrainingRequest(String trainerUsername, String traineeUsername, String name, String trainingTypeName, Date date, Integer duration) {
        this.trainerUsername = trainerUsername;
        this.traineeUsername = traineeUsername;
        this.name = name;
        this.trainingTypeName = trainingTypeName;
        this.date = date;
        this.duration = duration;
    }
}
