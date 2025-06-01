package com.dbenavidess.gym_part_1.infrastructure.repository.jpa.entitites;

import com.dbenavidess.gym_part_1.domain.model.Trainee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "trainee")
public class TraineeEntity {

    @Id
//    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Date dateOfBirth;

    @OneToMany(mappedBy = "trainee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrainingEntity> trainings;

    @ManyToMany()
    @JoinTable(name = "trainee_trainer",
    joinColumns = @JoinColumn( name = "trainee_id"),
    inverseJoinColumns = @JoinColumn(name = "trainer_id"))
    private List<TrainerEntity> trainers = new ArrayList<>();

    public TraineeEntity(UUID id, UserEntity user, String address, Date dateOfBirth) {
        this.id = id;
        this.user = user;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
    }

    public static TraineeEntity of(Trainee trainee) {

        UserEntity userEntity = UserEntity.of(trainee.getUser());
        return new TraineeEntity(
                trainee.getId(),
                userEntity,
                trainee.getAddress(),
                trainee.getDateOfBirth()
        );
    }

    public Trainee toDomain() {
        Trainee res =  new Trainee(
                this.id,
                this.address,
                this.dateOfBirth,
                this.user.toDomain()
        );

        return res;
    }
}