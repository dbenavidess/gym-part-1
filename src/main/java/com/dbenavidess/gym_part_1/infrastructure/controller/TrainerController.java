package com.dbenavidess.gym_part_1.infrastructure.controller;

import com.dbenavidess.gym_part_1.application.TrainerService;
import com.dbenavidess.gym_part_1.application.UserService;
import com.dbenavidess.gym_part_1.domain.model.Trainer;
import com.dbenavidess.gym_part_1.domain.model.TrainingType;
import com.dbenavidess.gym_part_1.domain.model.User;
import com.dbenavidess.gym_part_1.domain.repository.TrainingTypeRepository;
import com.dbenavidess.gym_part_1.domain.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class TrainerController {

    private static final Logger logger = LoggerFactory.getLogger(TrainerController.class);
    private final TrainerService service;
    private final UserService userService;
    private UserRepository userRepository;
    private TrainingTypeRepository trainingTypeRepository;

    public TrainerController(TrainerService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @PostMapping("/trainer")
    public ResponseEntity<Trainer> createTrainer(@RequestBody Map<String,String> body){
        boolean isActive = body.get("isActive").equals("true");
        try{
            TrainingType type = trainingTypeRepository.getById(UUID.fromString(body.get("specialization")));
            User user = new User(body.get("firstName"), body.get("lastName"), isActive, userRepository);
            Trainer trainer = new Trainer(type,user);

            return new ResponseEntity<>(service.createTrainer(trainer), HttpStatus.CREATED);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/trainer")
    public ResponseEntity<Trainer> updateTrainer(@RequestBody Trainer trainer){
        try{
            return new ResponseEntity<>(service.updateTrainer(trainer), HttpStatus.OK);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/trainer/{id}")
    public ResponseEntity<Trainer> getTrainer(@PathVariable UUID id){
        Trainer trainer = service.getTrainer(id);
        if (trainer == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(trainer,HttpStatus.OK);
    }

    @GetMapping("/trainer/search-username/{username}")
    public ResponseEntity<Trainer> getTrainerByUsername(@PathVariable String username ){
        Trainer trainer = service.getTrainerByUsername(username);
        if (trainer == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(trainer,HttpStatus.OK);
    }

    @PostMapping("/trainer/login")
    public ResponseEntity<Boolean> login(@RequestBody Map<String,String> body){
        boolean result = userService.login(body.get("username"),body.get("password"));
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @PostMapping("/trainer/change-password")
    public ResponseEntity<Boolean> changePassword(@RequestBody Map<String,String> body){
        boolean result = userService.changePassword(UUID.fromString(body.get("id")),body.get("password"));
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @PostMapping("/trainer/change-active")
    public ResponseEntity<Boolean> changeActive(@RequestBody Map<String,String> body){
        boolean result = userService.changeActiveStatus(UUID.fromString(body.get("id")));
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @GetMapping("/trainer")
    public List<Trainer> getTrainer(){
        return service.getAllTrainers();
    }

    @Autowired
    void setUserRepository(UserRepository repository){
        this.userRepository = repository;
    }

}
