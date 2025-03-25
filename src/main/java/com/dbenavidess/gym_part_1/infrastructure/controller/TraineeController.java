package com.dbenavidess.gym_part_1.infrastructure.controller;

import com.dbenavidess.gym_part_1.application.TraineeService;
import com.dbenavidess.gym_part_1.domain.model.Trainee;
import com.dbenavidess.gym_part_1.domain.model.Trainer;
import com.dbenavidess.gym_part_1.domain.model.TrainingType;
import com.dbenavidess.gym_part_1.domain.model.User;
import com.dbenavidess.gym_part_1.domain.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.Date;
import java.util.Map;

public class TraineeController {

    private static final Logger logger = LoggerFactory.getLogger(TrainerController.class);

    private final TraineeService service;
    private UserRepository userRepository;

    public TraineeController(TraineeService service) {
        this.service = service;
    }

    @PostMapping("/trainer")
    public ResponseEntity<Trainee> createTrainer(@RequestBody Map<String,String> body){
        boolean isActive = body.get("isActive").equals("true");
        try{

            User user = new User(body.get("firstName"), body.get("lastName"), isActive, userRepository);
            Date date = Date.valueOf(body.get("dateOfBirth"));
            Trainee trainee = new Trainee(body.get("address"),date,user);

            return new ResponseEntity<>(service.createTrainee(trainee), HttpStatus.CREATED);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
    }
}
