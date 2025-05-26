package com.dbenavidess.gym_part_1.infrastructure.response;

import com.dbenavidess.gym_part_1.domain.model.Trainee;
import com.dbenavidess.gym_part_1.domain.model.Trainer;
import com.dbenavidess.gym_part_1.domain.model.TrainingType;
import org.springframework.hateoas.RepresentationModel;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class TraineeProfileResponse extends RepresentationModel<TraineeProfileResponse> {
    public String username;
    public String firstName;
    public String lastName;
    public Date dateOfBirth;
    public String address;
    public boolean isActive;
    public List<TraineeTrainerRepr> trainersList = null;
    public static class TraineeTrainerRepr{
        public String username;
        public String firstName;
        public String lastName;
        public TrainingType specialization;

        public static List<TraineeTrainerRepr> fromTrainerList(List<Trainer> list){
            List<TraineeTrainerRepr> trainersList = new ArrayList<>();

            for (Trainer trainer : list){
                trainersList.add(new TraineeTrainerRepr(
                        trainer.getUser().getUsername(),
                        trainer.getUser().getFirstName(),
                        trainer.getUser().getLastName(),
                        trainer.getSpecialization()));
            }
            return trainersList;
        }


        public TraineeTrainerRepr(String username, String firstName, String lastName, TrainingType specialization) {
            this.username = username;
            this.firstName = firstName;
            this.lastName = lastName;
            this.specialization = specialization;
        }

    }

    public TraineeProfileResponse() {
    }

    public TraineeProfileResponse(Trainee trainee, List<Trainer> trainers) {
        this.username = trainee.getUser().getUsername();
        this.firstName = trainee.getUser().getFirstName();
        this.lastName = trainee.getUser().getLastName();
        this.isActive = trainee.getUser().getIsActive();
        this.dateOfBirth = trainee.getDateOfBirth();
        this.address = trainee.getAddress();
        this.trainersList = TraineeTrainerRepr.fromTrainerList(trainers);

    }
}
