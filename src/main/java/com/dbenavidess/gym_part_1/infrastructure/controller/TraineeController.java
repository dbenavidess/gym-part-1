package com.dbenavidess.gym_part_1.infrastructure.controller;

import com.dbenavidess.gym_part_1.service.TraineeService;
import com.dbenavidess.gym_part_1.domain.model.Trainee;
import com.dbenavidess.gym_part_1.domain.model.Trainer;
import com.dbenavidess.gym_part_1.domain.model.User;
import com.dbenavidess.gym_part_1.domain.repository.UserRepository;
import com.dbenavidess.gym_part_1.infrastructure.request.Trainee.CreateTraineeRequest;
import com.dbenavidess.gym_part_1.infrastructure.request.Trainee.UpdateTraineeRequest;
import com.dbenavidess.gym_part_1.infrastructure.response.SignupResponse;
import com.dbenavidess.gym_part_1.infrastructure.response.TraineeProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Tag(name = "Trainees", description = "Trainee operations")
public class TraineeController {

    private final TraineeService service;

    private final UserRepository userRepository;

    public TraineeController(TraineeService service, UserRepository userRepository) {
        this.service = service;
        this.userRepository = userRepository;
    }

    @Operation(summary = "Create trainee")
    @ApiResponse(responseCode = "201", description = "Trainee created",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = SignupResponse.class)) })
    @PostMapping("/trainee")
    public ResponseEntity<SignupResponse> createTrainee(@RequestBody CreateTraineeRequest body){
        User user = new User(body.firstName, body.lastName, true, userRepository);
        Trainee trainee = service.createTrainee(new Trainee(body.address,body.dateOfBirth,user));
        SignupResponse response = new SignupResponse(trainee.getUser().getUsername(),trainee.getUser().getPassword());
        response.add(linkTo(methodOn(TraineeController.class).getTrainee(trainee.getUser().getUsername())).withSelfRel());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Update trainee")
    @ApiResponse(responseCode = "200", description = "Trainee updated",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TraineeProfileResponse.class)) })
    @PutMapping("/trainee")
    public ResponseEntity<TraineeProfileResponse> updateTrainee(@RequestBody UpdateTraineeRequest body){
        User user = new User(body.username,body.firstName, body.lastName, body.isActive);
        Trainee trainee = service.updateTrainee(new Trainee(
                body.address,
                body.dateOfBirth,
                user
        ));
        return new ResponseEntity<>(new TraineeProfileResponse(trainee,service.getTrainerList(trainee)), HttpStatus.OK);
    }

    @Operation(summary = "Get trainee")
    @ApiResponse(responseCode = "200", description = "Get trainee by username",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TraineeProfileResponse.class)) })
    @GetMapping("/trainee/{username}")
    public ResponseEntity<TraineeProfileResponse> getTrainee(@PathVariable String username){
        Trainee trainee = service.getTraineeByUsername(username);
        if (trainee == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new TraineeProfileResponse(trainee,service.getTrainerList(trainee)),HttpStatus.OK);
    }

    @Operation(summary = "Delete trainee")
    @ApiResponse(responseCode = "200", description = "Delete trainee by username")
    @DeleteMapping("/trainee/{username}")
    public ResponseEntity<String> deleteTrainer(@PathVariable String username){
        service.deleteByUsername(username);
        return new ResponseEntity<>(HttpStatus.OK);

    }
    @Operation(summary = "Get  not assigned trainer list")
    @ApiResponse(responseCode = "200", description = "Get  not assigned trainers to trainee by username",
            content = { @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = TraineeProfileResponse.TraineeTrainerRepr.class))) })
    @GetMapping("/trainee/{username}/get-not-assigned-trainers")
    public ResponseEntity<List<TraineeProfileResponse.TraineeTrainerRepr>> getNotAssignedTrainerList(@PathVariable String username){
        List<Trainer> trainers = service.getNotAssignedTrainerList(service.getTraineeByUsername(username));
        return new ResponseEntity<>(TraineeProfileResponse.TraineeTrainerRepr.fromTrainerList(trainers),HttpStatus.OK);
    }
    @Operation(summary = "Update trainee's trainer list")
    @ApiResponse(responseCode = "200", description = "Update trainee's assigned trainers",
            content = { @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = TraineeProfileResponse.TraineeTrainerRepr.class))) })
    @PutMapping("/trainee/{username}/update-trainers")
    public ResponseEntity<List<TraineeProfileResponse.TraineeTrainerRepr>> updateTraineeTrainersList(@PathVariable String username, @RequestBody List<String> body){
        List<TraineeProfileResponse.TraineeTrainerRepr> list = TraineeProfileResponse.TraineeTrainerRepr
                .fromTrainerList(service.updateTraineeTrainerList(body, username));
        return new ResponseEntity<>(list,HttpStatus.OK);
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