package com.dbenavidess.gym_part_1.domain.model;
import lombok.Getter;

import java.util.UUID;


@Getter
public class Trainer{
    private final UUID id;
    private final User user;
    private final TrainingType specialization;

    public Trainer(TrainingType specialization, User user) {
        this.id = UUID.randomUUID();
        this.specialization = specialization;
        this.user = user;
    }
}
