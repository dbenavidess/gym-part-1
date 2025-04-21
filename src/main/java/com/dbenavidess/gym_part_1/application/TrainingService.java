package com.dbenavidess.gym_part_1.application;

import com.dbenavidess.gym_part_1.domain.model.Trainee;
import com.dbenavidess.gym_part_1.domain.model.Trainer;
import com.dbenavidess.gym_part_1.domain.model.Training;
import com.dbenavidess.gym_part_1.domain.model.TrainingType;
import com.dbenavidess.gym_part_1.domain.repository.TraineeRepository;
import com.dbenavidess.gym_part_1.domain.repository.TrainingRepository;
import com.dbenavidess.gym_part_1.domain.repository.TrainingTypeRepository;
import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.entitites.TrainingTypeEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Service
public class TrainingService {

    @Autowired
    TrainingRepository repository;
    @Autowired
    TrainingTypeRepository trainingTypeRepository;

    @Autowired
    TraineeService traineeService;
    @Autowired
    TrainerService trainerService;

    private static final Logger logger = LoggerFactory.getLogger(TrainingService.class);

    public Training createTraining(Training training){
        Training res = repository.createTraining(training);
        if(res == null){
            logger.error("Failed to create training: {}", training);
            throw new RuntimeException("Invalid Training");
        }
        logger.info("Successfully created training with ID: {}", res.getId());
        return res;
    }

    public Training getTraining(UUID id){
        Training training = repository.getTraining(id);
        if (training == null) {
            logger.warn("training with ID {} not found", id);
        } else {
            logger.info("Successfully retrieved training: {}", training);
        }
        return training;
    }

    public List<Training> searchTrainings(String trainerUsername, Date from, Date to, String traineeUsername, String trainingTypeName){
        Trainee trainee = traineeService.getTraineeByUsername(traineeUsername);
        Trainer trainer = trainerService.getTrainerByUsername(trainerUsername);
        TrainingType type = trainingTypeRepository.getByName(trainingTypeName);
        return repository.searchTrainings(from,to,trainer,trainee,type);
    }

}
