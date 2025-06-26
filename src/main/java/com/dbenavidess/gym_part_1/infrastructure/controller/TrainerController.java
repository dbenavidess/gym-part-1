package com.dbenavidess.gym_part_1.infrastructure.controller;

import com.dbenavidess.gym_part_1.config.security.service.JwtService;
import com.dbenavidess.gym_part_1.config.security.userDetails.UserDetailsModel;
import com.dbenavidess.gym_part_1.domain.util.PasswordEncryptionProvider;
import com.dbenavidess.gym_part_1.service.TrainerService;
import com.dbenavidess.gym_part_1.domain.model.Trainer;
import com.dbenavidess.gym_part_1.domain.model.TrainingType;
import com.dbenavidess.gym_part_1.domain.model.User;
import com.dbenavidess.gym_part_1.domain.repository.TrainingTypeRepository;
import com.dbenavidess.gym_part_1.domain.repository.UserRepository;
import com.dbenavidess.gym_part_1.infrastructure.request.Trainer.CreateTrainerRequest;
import com.dbenavidess.gym_part_1.infrastructure.request.Trainer.UpdateTrainerRequest;
import com.dbenavidess.gym_part_1.infrastructure.response.SignupResponse;
import com.dbenavidess.gym_part_1.infrastructure.response.TrainerProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.NoSuchElementException;

@RestController
public class TrainerController {

    private final TrainerService service;
    private final UserRepository userRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final PasswordEncryptionProvider passwordEncryptionProvider;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public TrainerController(TrainerService service,
                             TrainingTypeRepository trainingTypeRepository,
                             UserRepository userRepository,
                             PasswordEncryptionProvider passwordEncryptionProvider,
                             JwtService jwtService,
                             AuthenticationManager authenticationManager
                             ){
        this.service = service;
        this.trainingTypeRepository = trainingTypeRepository;
        this.userRepository = userRepository;
        this.passwordEncryptionProvider = passwordEncryptionProvider;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }
    @Operation(summary = "Create trainer")
    @ApiResponse(responseCode = "201", description = "Trainer created",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = SignupResponse.class)) })
    @PostMapping("/trainer")
    public ResponseEntity<SignupResponse> createTrainer(@RequestBody CreateTrainerRequest body){
        TrainingType type = trainingTypeRepository.getByName(body.specialization);
        User user = new User(body.firstName, body.lastName, true, userRepository,passwordEncryptionProvider);
        Trainer trainer = service.createTrainer(new Trainer(type,user));
        String jwt = jwtService.generateToken(new HashMap<>(),new UserDetailsModel(trainer.getUser()));

        return new ResponseEntity<>(new SignupResponse(trainer.getUser().getUsername(),trainer.getUser().getPassword(),jwt), HttpStatus.CREATED);
    }

    @Operation(summary = "Update trainer")
    @ApiResponse(responseCode = "200", description = "Trainer updated",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UpdateTrainerRequest.class)) })
    @PutMapping("/trainer")
    public ResponseEntity<TrainerProfileResponse> updateTrainer(@RequestBody UpdateTrainerRequest body){
        User user = new User(body.username,body.firstName, body.lastName, body.isActive);
        TrainingType specialization = trainingTypeRepository.getByName(body.specialization);
        Trainer trainer = service.updateTrainer(new Trainer(specialization,user));


        return new ResponseEntity<>(new TrainerProfileResponse(trainer,service.getTrainees(trainer)), HttpStatus.OK);
    }
    @Operation(summary = "Get trainer")
    @ApiResponse(responseCode = "200", description = "Get trainer by username",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TrainerProfileResponse.class)) })
    @GetMapping("/trainer/{username}")
    public ResponseEntity<TrainerProfileResponse> getTrainer(@PathVariable String username ){
        Trainer trainer = service.getTrainerByUsername(username);
        if (trainer == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new TrainerProfileResponse(trainer,service.getTrainees(trainer)),HttpStatus.OK);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException e){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

}
