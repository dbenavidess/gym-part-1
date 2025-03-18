package com.dbenavidess.gym_part_1.application;

import com.dbenavidess.gym_part_1.domain.model.Trainer;

import com.dbenavidess.gym_part_1.domain.repository.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TrainerService {

    @Autowired
    TrainerRepository repository;

    public Trainer createTrainer(Trainer trainer){
        return repository.createTrainer(trainer);
    }

    public Trainer updateTrainer(Trainer trainer){
        return repository.updateTrainer(trainer);
    }

    public void deleteTrainer(Trainer trainer){
        repository.deleteTrainer(trainer);
    }

    public Trainer getTrainer(UUID id){
        return repository.getTrainer(id);
    }
}
