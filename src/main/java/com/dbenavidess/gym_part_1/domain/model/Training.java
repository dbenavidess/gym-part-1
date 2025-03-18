package com.dbenavidess.gym_part_1.domain.model;

import lombok.Getter;

import java.util.Date;
import java.util.UUID;

@Getter
public class Training {
    private final UUID id;
    private final Trainer trainer;
//    private long trainerId;
    private final Trainee trainee;
//    private long traineeId;
    private final String name;
    private final TrainingType type;
    private final Date date;
    private final int duration;

    public Training(Trainer trainer, Trainee trainee, String name, TrainingType type, Date date, int duration) {
        this.id = UUID.randomUUID();
        this.trainer = trainer;
        this.trainee = trainee;
        this.name = name;
        this.type = type;
        this.date = date;
        this.duration = duration;
    }
}
