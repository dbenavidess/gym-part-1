package com.dbenavidess.gym_part_1.infrastructure.response;

import com.dbenavidess.gym_part_1.domain.model.Training;
import com.dbenavidess.gym_part_1.domain.model.TrainingType;

import java.sql.Date;
import java.util.List;

public class TrainingDetailsResponse {
    public String name;
    public Date date;
    public TrainingType type;
    public Integer duration;
    public String trainerName;
    public String traineeName;

    public static List<TrainingDetailsResponse> ofTrainingList(List<Training> list){
        return list.stream()
                .map(TrainingDetailsResponse::new)
                .toList();
    }

    public TrainingDetailsResponse(){

    }

    public TrainingDetailsResponse(Training training) {
        this.name = training.getName();
        this.date = training.getDate();
        this.type = training.getType();
        this.duration = training.getDuration();
        this.trainerName = training.getTrainer().getUser().getFirstName() + training.getTrainer().getUser().getLastName();
        this.traineeName = training.getTrainee().getUser().getFirstName() + training.getTrainee().getUser().getLastName();
    }

}
