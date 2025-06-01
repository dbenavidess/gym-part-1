package com.dbenavidess.gym_part_1.application.service;

import com.dbenavidess.gym_part_1.domain.model.Trainee;
import com.dbenavidess.gym_part_1.domain.model.Trainer;
import com.dbenavidess.gym_part_1.domain.model.Training;
import com.dbenavidess.gym_part_1.domain.model.TrainingType;
import com.dbenavidess.gym_part_1.domain.repository.TrainingRepository;
import com.dbenavidess.gym_part_1.domain.repository.TrainingTypeRepository;
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

    public Training createTraining(String name, Date date, Integer duration,String trainingTypeName, String trainerUsername, String traineeUsername){

        Trainer trainer = trainerService.getTrainerByUsername(trainerUsername);
        Trainee trainee = traineeService.getTraineeByUsername(traineeUsername);
        TrainingType trainingType = trainingTypeRepository.getByName(trainingTypeName);

        Training res = repository.createTraining(new Training(trainer,trainee,name,trainingType, date, duration));
        if(res == null){
            throw new RuntimeException("Invalid Training");
        }
        return res;
    }

    public Training getTraining(UUID id){
        return repository.getTraining(id);
    }

    public List<Training> searchTrainings(String trainerUsername, Date from, Date to, String traineeUsername, String trainingTypeName){

        Trainee trainee = null;
        Trainer trainer = null;
        TrainingType type = null;
        if(traineeUsername != null && !traineeUsername.isEmpty()) {
            trainee = traineeService.getTraineeByUsername(traineeUsername);
        }
        if(trainerUsername != null && !trainerUsername.isEmpty()){
            trainer = trainerService.getTrainerByUsername(trainerUsername);
        }
        if(trainingTypeName != null && !trainingTypeName.isEmpty()){
            type = trainingTypeRepository.getByName(trainingTypeName);
        }

        return repository.searchTrainings(from,to,trainer,trainee,type);
    }

    public void deleteTraining(UUID id) {
        repository.deleteTraining(id);
    }
}