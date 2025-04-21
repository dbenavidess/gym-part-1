package com.dbenavidess.gym_part_1.infrastructure.controller;

import com.dbenavidess.gym_part_1.application.TraineeService;
import com.dbenavidess.gym_part_1.application.UserService;
import com.dbenavidess.gym_part_1.domain.model.Trainee;
import com.dbenavidess.gym_part_1.domain.model.Trainer;
import com.dbenavidess.gym_part_1.domain.model.User;
import com.dbenavidess.gym_part_1.domain.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
@RestController
public class TraineeController {

    private static final Logger logger = LoggerFactory.getLogger(TrainerController.class);

    private final TraineeService service;
    private final UserService userService;
    private UserRepository userRepository;


    public TraineeController(TraineeService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @PostMapping("/trainee")
    public ResponseEntity<Trainee> createTrainee(@RequestBody Map<String,String> body){
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

    @PutMapping("/trainee")
    public ResponseEntity<Trainee> updateTrainee(@RequestBody Trainee trainee){
        try{
            return new ResponseEntity<>(service.updateTrainee(trainee), HttpStatus.OK);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/trainee/{id}")
    public ResponseEntity<Trainee> getTrainee(@PathVariable UUID id){
        Trainee trainee = service.getTrainee(id);
        if (trainee == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(trainee,HttpStatus.OK);
    }

    @DeleteMapping("/trainee/{id}")
    public ResponseEntity<String> deleteTrainer(@PathVariable UUID id){
        try{
            service.deleteTrainee(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/trainee/search-username/{username}")
    public ResponseEntity<Trainee> getTraineeByUsername(@PathVariable String username ){
        Trainee trainee = service.getTraineeByUsername(username);
        if (trainee == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(trainee,HttpStatus.OK);
    }

    @PostMapping("/trainee/login")
    public ResponseEntity<Boolean> login(@RequestBody Map<String,String> body){
        boolean result = userService.login(body.get("username"),body.get("password"));
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @PostMapping("/trainee/change-password")
    public ResponseEntity<Boolean> changePassword(@RequestBody Map<String,String> body){
        boolean result = userService.changePassword(UUID.fromString(body.get("id")),body.get("password"));
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @PostMapping("/trainee/change-active")
    public ResponseEntity<Boolean> changeActive(@RequestBody Map<String,String> body){
        boolean result = userService.changeActiveStatus(UUID.fromString(body.get("id")));
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @DeleteMapping("/trainee-username/{id}")
    public ResponseEntity<String> deleteTrainerByUsername(@PathVariable String username){
        try{
            service.deleteByUsername(username);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/trainee/get-not-assigned-trainers/{username}")
    public ResponseEntity<List<Trainer>> getNotAssignedTrainerList(@PathVariable String username ){

        List<Trainer> trainers = service.getNotAssignedTrainerList(service.getTraineeByUsername(username));
        if (trainers == null || trainers.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(trainers,HttpStatus.OK);
    }

    @Autowired
    void setUserRepository(UserRepository repository){
        this.userRepository = repository;
    }

}
