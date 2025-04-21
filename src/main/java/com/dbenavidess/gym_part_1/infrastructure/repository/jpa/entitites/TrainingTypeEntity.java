package com.dbenavidess.gym_part_1.infrastructure.repository.jpa.entitites;

import com.dbenavidess.gym_part_1.domain.model.TrainingType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "training_type")
public class TrainingTypeEntity {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "specialization", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrainerEntity> trainer;

    @OneToMany(mappedBy = "type", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrainingEntity> training;

    public TrainingTypeEntity() {
    }

    public TrainingTypeEntity(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public static TrainingTypeEntity of(TrainingType type) {
        return new TrainingTypeEntity(type.getId(),type.getName());
    }

    public TrainingType toDomain() {
        return new TrainingType(id,name);
    }
}
