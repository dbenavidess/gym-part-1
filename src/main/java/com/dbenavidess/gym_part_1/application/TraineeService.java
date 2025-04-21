package com.dbenavidess.gym_part_1.application;

import com.dbenavidess.gym_part_1.domain.model.Trainee;
import com.dbenavidess.gym_part_1.domain.model.Trainer;
import com.dbenavidess.gym_part_1.domain.repository.TraineeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TraineeService {
    @Autowired
    TraineeRepository repository;

    private static final Logger logger = LoggerFactory.getLogger(TraineeService.class);


    public Trainee createTrainee(Trainee trainee){

        Trainee res = repository.createTrainee(trainee);
        if (res == null || trainee.getUser() == null){
            logger.error("Failed to create trainee: {}", trainee);
            throw new RuntimeException("Invalid Trainee");
        }
        logger.info("Successfully created trainee with ID: {}", res.getId());
        return res;
    }

    public void deleteTrainee(UUID id){
        repository.deleteTrainee(id);
        logger.info("Successfully deleted trainee with ID: {}", id);
    }

    public Trainee updateTrainee(Trainee trainee){
        Trainee updatedTrainee = repository.updateTrainee(trainee);
        logger.info("Successfully updated trainee with ID: {}", updatedTrainee.getId());
        return updatedTrainee;
    }

    public Trainee getTrainee(UUID id){
        Trainee trainee = repository.getTrainee(id);
        if (trainee == null) {
            logger.warn("Trainee with ID {} not found", id);
        } else {
            logger.info("Successfully retrieved trainee: {}", trainee);
        }
        return trainee;
    }

    public Trainee getTraineeByUsername(String username){
        Trainee trainee = repository.getByUsername(username);
        if (trainee == null) {
            logger.warn("Trainee with Username {} not found", username);
        } else {
            logger.info("Successfully retrieved trainee: {}", trainee);
        }
        return trainee;
    }

    public List<Trainer> getTrainerList(Trainee trainee){
        return repository.getTrainers(trainee);
    }

    public List<Trainer> getNotAssignedTrainerList(Trainee trainee){
        return repository.getNotAssignedTrainers(trainee);
    }

    public void deleteByUsername(String username){
        repository.deleteByUsername(username);
        logger.info("Successfully deleted trainee with username: {}", username);
    }

    public List<Trainer> addTrainerToTrainee(Trainee trainee, Trainer trainer) {
        return repository.addTrainerToTrainee(trainee.getId(),trainer.getId());
    }

}
