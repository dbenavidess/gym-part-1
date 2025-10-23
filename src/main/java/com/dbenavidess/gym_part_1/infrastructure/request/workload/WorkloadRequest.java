package com.dbenavidess.gym_part_1.infrastructure.request.workload;

import java.time.LocalDate;

public class WorkloadRequest {

    private String trainerUsername;
    private String trainerFirstName;
    private String trainerLastName;
    private boolean isActive;
    private LocalDate trainingDate;
    private int trainingDuration;
    private String actionType;


    public String getTrainerUsername() {
        return trainerUsername;
    }

    public void setTrainerUsername(String trainerUsername) {
        this.trainerUsername = trainerUsername;
    }

    public String getTrainerFirstName() {
        return trainerFirstName;
    }

    public void setTrainerFirstName(String trainerFirstName) {
        this.trainerFirstName = trainerFirstName;
    }

    public String getTrainerLastName() {
        return trainerLastName;
    }

    public void setTrainerLastName(String trainerLastName) {
        this.trainerLastName = trainerLastName;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }

    public LocalDate getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(LocalDate trainingDate) {
        this.trainingDate = trainingDate;
    }

    public int getTrainingDuration() {
        return trainingDuration;
    }

    public void setTrainingDuration(int trainingDuration) {
        this.trainingDuration = trainingDuration;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }
}
