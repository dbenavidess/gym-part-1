package com.dbenavidess.gym_part_1.infrastructure.repository.jpa.entitites;

import com.dbenavidess.gym_part_1.domain.model.Trainer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "trainer")
public class TrainerEntity {

    @Id
    private UUID id;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne()
    @JoinColumn(name = "specialization", nullable = false)
    private TrainingTypeEntity specialization;

    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrainingEntity> trainings;

    @ManyToMany(mappedBy = "trainers")
    private List<TraineeEntity> trainees = new ArrayList<>();

    public TrainerEntity(TrainingTypeEntity specialization, UserEntity user, UUID id) {
        this.specialization = specialization;
        this.user = user;
        this.id = id;
    }

    public static TrainerEntity of(Trainer trainer) {
        TrainingTypeEntity spec = TrainingTypeEntity.of(trainer.getSpecialization());
        UserEntity user = UserEntity.of(trainer.getUser());

        return new TrainerEntity(
                spec,
                user,
                trainer.getId()
        );
    }

    public Trainer toDomain(){
        Trainer res = new Trainer(this.id,
                this.user.toDomain(),
                this.specialization.toDomain());
        return res;
    }
}