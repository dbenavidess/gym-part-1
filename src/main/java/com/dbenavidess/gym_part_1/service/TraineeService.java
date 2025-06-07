package com.dbenavidess.gym_part_1.service;

import com.dbenavidess.gym_part_1.domain.model.Trainee;
import com.dbenavidess.gym_part_1.domain.model.Trainer;
import com.dbenavidess.gym_part_1.domain.repository.TraineeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class TraineeService {
    @Autowired
    TraineeRepository repository;

    public Trainee createTrainee(Trainee trainee){
        Trainee res = repository.createTrainee(trainee);
        if (res == null || trainee.getUser() == null){
            throw new IllegalArgumentException("Invalid Trainee");
        }
        return res;
    }

    public void deleteTrainee(UUID id){
        repository.deleteTrainee(id);
    }

    public Trainee updateTrainee(Trainee trainee){
        return repository.updateTrainee(trainee);
    }

    public Trainee getTraineeByUsername(String username){
        return repository.getByUsername(username);
    }

    public List<Trainer> getTrainerList(Trainee trainee){
        return repository.getTrainers(trainee);
    }

    public List<Trainer> getNotAssignedTrainerList(Trainee trainee){
        return repository.getNotAssignedTrainers(trainee);
    }

    public void deleteByUsername(String username){
        repository.deleteByUsername(username);
    }

    public List<Trainer> updateTraineeTrainerList(List<String> trainers, String username){
        Trainee trainee = repository.getByUsername(username);
        if (trainee == null) {
            throw new NoSuchElementException();
        }
        return repository.updateTraineeTrainerList(trainers, trainee);
    }

}
