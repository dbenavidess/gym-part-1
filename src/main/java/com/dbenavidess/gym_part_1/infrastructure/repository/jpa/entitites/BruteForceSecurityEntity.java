package com.dbenavidess.gym_part_1.infrastructure.repository.jpa.entitites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Entity
@Getter
@Setter
@Table(name = "BruteForceSecurity")
public class BruteForceSecurityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private int failedAttemptsCounter;
    private Date unlockDate;

    @OneToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    public BruteForceSecurityEntity() {
    }

    public BruteForceSecurityEntity(UserEntity user) {
        this.failedAttemptsCounter = 0;
        this.user = user;
    }

}
