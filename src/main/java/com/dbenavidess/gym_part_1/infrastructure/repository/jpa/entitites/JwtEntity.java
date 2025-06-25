package com.dbenavidess.gym_part_1.infrastructure.repository.jpa.entitites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class JwtEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private String jwt;

    @OneToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    public JwtEntity() {
    }

    public JwtEntity(String jwt, UserEntity user) {
        this.jwt = jwt;
        this.user = user;
    }
}
