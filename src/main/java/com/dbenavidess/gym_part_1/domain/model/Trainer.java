package com.dbenavidess.gym_part_1.domain.model;
import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;


@Getter
public class Trainer implements Serializable {
    private UUID id;
    private User user;
    private TrainingType specialization;

    public Trainer() {
    }

    public Trainer(TrainingType specialization, User user) {
        this.id = UUID.randomUUID();
        this.specialization = specialization;
        this.user = user;
    }

    public Trainer(UUID id, User user, TrainingType specialization) {
        this.id = id;
        this.user = user;
        this.specialization = specialization;
    }
}
