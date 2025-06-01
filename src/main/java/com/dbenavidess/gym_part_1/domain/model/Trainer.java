package com.dbenavidess.gym_part_1.domain.model;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode
public class Trainer {
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
