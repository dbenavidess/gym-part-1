package com.dbenavidess.gym_part_1.infrastructure.response;

import com.dbenavidess.gym_part_1.domain.model.Trainee;
import com.dbenavidess.gym_part_1.domain.model.Trainer;
import com.dbenavidess.gym_part_1.domain.model.TrainingType;
import org.springframework.hateoas.RepresentationModel;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class TrainerProfileResponse extends RepresentationModel<TrainerProfileResponse> {
    public String username;
    public String firstName;
    public String lastName;
    public TrainingType specialization;

    public boolean isActive;
    public List<TrainerTraineeRepr> trainersList = null;
    public static class TrainerTraineeRepr{
        public String username;
        public String firstName;
        public String lastName;
        public Date dateOfBirth;
        public String address;

        public static List<TrainerTraineeRepr> fromTraineeList(List<Trainee> list){
            List<TrainerTraineeRepr> traineesList = new ArrayList<>();

            for (Trainee trainee : list){
                traineesList.add(new TrainerTraineeRepr(
                        trainee.getUser().getUsername(),
                        trainee.getUser().getFirstName(),
                        trainee.getUser().getLastName(),
                        trainee.getDateOfBirth(),
                        trainee.getAddress()));
            }
            return traineesList;
        }

        public TrainerTraineeRepr(String username, String firstName, String lastName, Date dateOfBirth, String address) {
            this.username = username;
            this.firstName = firstName;
            this.lastName = lastName;
            this.dateOfBirth = dateOfBirth;
            this.address = address;
        }
    }

    public TrainerProfileResponse(){

    }

    public TrainerProfileResponse(Trainer trainer, List<Trainee> trainees) {
        this.username = trainer.getUser().getUsername();
        this.firstName = trainer.getUser().getFirstName();
        this.lastName = trainer.getUser().getLastName();
        this.isActive = trainer.getUser().getIsActive();
        this.specialization = trainer.getSpecialization();
        this.trainersList = TrainerTraineeRepr.fromTraineeList(trainees);

    }
}
