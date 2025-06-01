package com.dbenavidess.gym_part_1.application.service;

import com.dbenavidess.gym_part_1.domain.model.Trainee;
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
            throw new IllegalArgumentException("Invalid Trainer");
        }
        return res;
    }

    public Trainer updateTrainer(Trainer trainer){
        return repository.updateTrainer(trainer);
    }

    public List<Trainee> getTrainees(Trainer trainer){
        return repository.getTrainees(trainer);
    }

    public Trainer getTrainerByUsername(String username){

        return repository.getByUsername(username);
    }

    public List<Trainer> getAllTrainers(){
        return repository.getAllTrainers();
    }

    public void deleteTrainer(UUID id) {
        repository.deleteTrainer(id);
    }
}
