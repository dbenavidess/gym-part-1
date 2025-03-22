package com.dbenavidess.gym_part_1.application;

import com.dbenavidess.gym_part_1.domain.model.Trainee;
import com.dbenavidess.gym_part_1.domain.repository.TraineeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TraineeService {
    @Autowired
    TraineeRepository repository;

    public Trainee createTrainee(Trainee trainee){

        Trainee res = repository.createTrainee(trainee);
        if (res == null){
            throw new RuntimeException("Invalid trainee");
        }
        return res;
    }

    public void deleteTrainee(UUID id){
        repository.deleteTrainee(id);
    }

    public Trainee updateTrainee(Trainee trainee){
        return repository.updateTrainee(trainee);
    }

    public Trainee getTrainee(UUID id){
        return repository.getTrainee(id);
    }

}
