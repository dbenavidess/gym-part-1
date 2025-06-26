package com.dbenavidess.gym_part_1.infrastructure.controller;

import com.dbenavidess.gym_part_1.service.TrainingService;
import com.dbenavidess.gym_part_1.domain.model.Training;
import com.dbenavidess.gym_part_1.infrastructure.request.Training.CreateTrainingRequest;
import com.dbenavidess.gym_part_1.infrastructure.response.TrainingDetailsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class TrainingController {

    private final TrainingService trainingService;

    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }
    @Operation(summary = "Create training")
    @ApiResponse(responseCode = "200", description = "Create training")
    @PostMapping("/training")
    public ResponseEntity<Training> createTraining(@RequestBody CreateTrainingRequest body){

        Training res = trainingService.createTraining(
                body.name,
                body.date,
                body.duration,
                body.trainingTypeName,
                body.trainerUsername,
                body.traineeUsername);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @Operation(summary = "Get trainer")
    @ApiResponse(responseCode = "200", description = "Get trainer by username",
            content = { @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = TrainingDetailsResponse.class))) })
    @GetMapping("/training")
    public ResponseEntity<List<TrainingDetailsResponse>> searchTrainings(
            @Parameter(description = "Trainer's username")
            @RequestParam(required = false) String trainerUsername,
            @Parameter(description = "Training type name")
            @RequestParam(required = false) String trainingTypeName,
            @Parameter(description = "Start date")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date from,
            @Parameter(description = "End date")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date to,
            @Parameter(description = "Trainee's username")
            @RequestParam(required = false) String traineeUsername) {

        if((traineeUsername == null || traineeUsername.isEmpty()) && (trainerUsername ==null || trainerUsername.isEmpty())){
            throw new IllegalArgumentException("Invalid Trainer/Trainee");
        }

        List<Training> trainings = trainingService.searchTrainings(trainerUsername, from, to, traineeUsername,trainingTypeName);
        return ResponseEntity.ok(TrainingDetailsResponse.ofTrainingList(trainings));
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