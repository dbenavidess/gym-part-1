package com.dbenavidess.gym_part_1.infrastructure.controller;

import com.dbenavidess.gym_part_1.application.TrainingService;
import com.dbenavidess.gym_part_1.domain.model.Training;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class TrainingController {

    TrainingService trainingService;

    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }
    @PostMapping("/training")
    public ResponseEntity<Training> createTraining(Training training){
        Training res = trainingService.createTraining(training);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @GetMapping("/training/{id}")
    public ResponseEntity<Training>getTraining(UUID id){
        return new ResponseEntity<>(trainingService.getTraining(id),HttpStatus.OK);
    }

}
