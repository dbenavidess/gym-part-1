package com.dbenavidess.gym_part_1.application;

import com.dbenavidess.gym_part_1.domain.model.Trainer;

import com.dbenavidess.gym_part_1.domain.repository.TrainerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TrainerService {

    @Autowired
    TrainerRepository repository;

    private static final Logger logger = LoggerFactory.getLogger(TrainerService.class);


    public Trainer createTrainer(Trainer trainer){

        Trainer res = repository.createTrainer(trainer);
        if (res == null){
            logger.error("Failed to create trainee: {}", trainer);
            throw new RuntimeException("Invalid Trainer");
        }
        logger.info("Successfully created trainee with ID: {}", res.getId());
        return res;
    }

    public Trainer updateTrainer(Trainer trainer){
        Trainer updatedTrainer = repository.updateTrainer(trainer);
        logger.info("Successfully updated trainee with ID: {}", updatedTrainer.getId());
        return updatedTrainer;
    }

    public Trainer getTrainer(UUID id){

        Trainer trainee = repository.getTrainer(id);
        if (trainee == null) {
            logger.warn("Trainee with ID {} not found", id);
        } else {
            logger.info("Successfully retrieved trainee: {}", trainee);
        }
        return trainee;
    }

    public List<Trainer> getAllTrainers(){
        return repository.getAllTrainers();
    }
}
