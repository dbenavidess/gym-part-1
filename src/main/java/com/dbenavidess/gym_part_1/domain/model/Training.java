package com.dbenavidess.gym_part_1.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode
public class Training implements Serializable {
    private UUID id;
    private Trainer trainer;
    private Trainee trainee;
    private String name;
    private TrainingType type;
    private LocalDate date;
    private int duration;

    public Training() {
    }

    public Training(Trainer trainer, Trainee trainee, String name, TrainingType type, LocalDate date, int duration) {
        this.id = UUID.randomUUID();
        this.trainer = trainer;
        this.trainee = trainee;
        this.name = name;
        this.type = type;
        this.date = date;
        this.duration = duration;
    }

    public Training(UUID id, Trainer trainer, Trainee trainee, String name, TrainingType type, LocalDate date, int duration) {
        this.id = id;
        this.trainer = trainer;
        this.trainee = trainee;
        this.name = name;
        this.type = type;
        this.date = date;
        this.duration = duration;
    }
}
