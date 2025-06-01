package com.dbenavidess.gym_part_1.domain.model;

import lombok.Getter;

import java.util.UUID;
@Getter
public class TrainingType {
    private UUID id;
    private String name; //fitness, yoga, zumba, stretching, resistance

    public TrainingType(){

    }

    public TrainingType(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public TrainingType(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
    }
}
