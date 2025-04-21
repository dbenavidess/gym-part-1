package com.dbenavidess.gym_part_1.infrastructure.repository.jpa.entitites;

import com.dbenavidess.gym_part_1.domain.model.Training;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "training")
public class TrainingEntity{

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "trainer_id", nullable = false)
    private TrainerEntity trainer;

    @ManyToOne
    @JoinColumn(name = "trainee_id", nullable = false)
    private TraineeEntity trainee;

    @Column(nullable = false)
    private String name;

    @ManyToOne()
    @JoinColumn(name = "type", nullable = false)
    private TrainingTypeEntity type;

    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    private int duration;

    public TrainingEntity(UUID id, TrainerEntity trainer, TraineeEntity trainee, String name, TrainingTypeEntity type, Date date, int duration) {
        this.id = id;
        this.trainer = trainer;
        this.trainee = trainee;
        this.name = name;
        this.type = type;
        this.date = date;
        this.duration = duration;
    }

    public static TrainingEntity of(Training training){
        return new TrainingEntity(
                training.getId(),
                TrainerEntity.of(training.getTrainer()),
                TraineeEntity.of(training.getTrainee()),
                training.getName(),
                TrainingTypeEntity.of(training.getType()),
                training.getDate(),
                training.getDuration()
        );
    }

    public Training toDomain(){
        return new Training(
                this.id,
                this.trainer.toDomain(),
                this.trainee.toDomain(),
                this.name,
                this.type.toDomain(),
                this.date,
                this.duration
        );
    }

}
