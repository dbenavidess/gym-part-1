package com.dbenavidess.gym_part_1.application;

import com.dbenavidess.gym_part_1.domain.model.Training;
import com.dbenavidess.gym_part_1.domain.repository.TrainingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TrainingService {
    @Autowired
    TrainingRepository repository;

    public Training createTraining(Training training){
        return repository.createTraining(training);
    }

    public Training getTraining(UUID id){
        return repository.getTraining(id);
    }

}
