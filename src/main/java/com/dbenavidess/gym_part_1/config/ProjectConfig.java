package com.dbenavidess.gym_part_1.config;

import com.dbenavidess.gym_part_1.domain.model.Trainee;
import com.dbenavidess.gym_part_1.domain.model.Trainer;
import com.dbenavidess.gym_part_1.domain.model.Training;
import com.dbenavidess.gym_part_1.domain.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
public class ProjectConfig {

    @Bean("trainerStorage")
    public Map<UUID, Trainer> trainerStorage(){
        return new HashMap<>();
    }

    @Bean("traineeStorage")
    public Map<UUID, Trainee> traineeStorage(){
        return new HashMap<>();
    }

    @Bean("trainingStorage")
    public Map<UUID, Training> trainingStorage(){
        return new HashMap<>();
    }

    @Bean("userStorage")
    public Map<UUID, User> userStorage(){
        return new HashMap<>();
    }

}
