package com.dbenavidess.domain;

import lombok.Getter;

import java.util.Date;

@Getter
public class Training {
    private final long id;
    private final Trainer trainer;
//    private long trainerId;
    private final Trainee trainee;
//    private long traineeId;
    private final String name;
    private final TrainingType type;
    private final Date date;
    private final int duration;

    public Training(long id, Trainer trainer, Trainee trainee, String name, TrainingType type, Date date, int duration) {
        this.id = id;
        this.trainer = trainer;
        this.trainee = trainee;
        this.name = name;
        this.type = type;
        this.date = date;
        this.duration = duration;
    }
}
