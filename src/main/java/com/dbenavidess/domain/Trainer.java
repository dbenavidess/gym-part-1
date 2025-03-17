package com.dbenavidess.domain;
import lombok.Getter;


@Getter
public class Trainer{
    private final User user;
    private final TrainingType specialization;

    public Trainer(TrainingType specialization, User user) {
        this.specialization = specialization;
        this.user = user;
    }
}
