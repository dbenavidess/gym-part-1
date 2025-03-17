package com.dbenavidess.domain;

import lombok.Getter;

@Getter
public class TrainingType {
    private final long id;
    private final String name; //fitness, yoga, Zumba, stretching, resistance

    public TrainingType(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
