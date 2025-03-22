package com.dbenavidess.gym_part_1.domain.model;

import lombok.Getter;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Getter
public class Training implements Serializable {
    private UUID id;
    private Trainer trainer;
    private Trainee trainee;
    private String name;
    private TrainingType type;
    private Date date;
    private int duration;

    public Training() {
    }

    public Training(Trainer trainer, Trainee trainee, String name, TrainingType type, Date date, int duration) {
        this.id = UUID.randomUUID();
        this.trainer = trainer;
        this.trainee = trainee;
        this.name = name;
        this.type = type;
        this.date = date;
        this.duration = duration;
    }

    public Training(UUID id, Trainer trainer, Trainee trainee, String name, TrainingType type, Date date, int duration) {
        this.id = id;
        this.trainer = trainer;
        this.trainee = trainee;
        this.name = name;
        this.type = type;
        this.date = date;
        this.duration = duration;
    }
}
