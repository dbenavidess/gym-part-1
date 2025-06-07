package com.dbenavidess.gym_part_1.controller;

import com.dbenavidess.gym_part_1.service.TraineeService;
import com.dbenavidess.gym_part_1.service.TrainerService;
import com.dbenavidess.gym_part_1.domain.model.Trainer;
import com.dbenavidess.gym_part_1.domain.model.User;
import com.dbenavidess.gym_part_1.domain.repository.TrainingTypeRepository;
import com.dbenavidess.gym_part_1.domain.repository.UserRepository;
import com.dbenavidess.gym_part_1.infrastructure.request.Trainer.CreateTrainerRequest;
import com.dbenavidess.gym_part_1.infrastructure.request.Trainer.UpdateTrainerRequest;
import com.dbenavidess.gym_part_1.infrastructure.response.SignupResponse;
import com.dbenavidess.gym_part_1.infrastructure.response.TrainerProfileResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TrainerControllerTest {
    private final String BASE_URI = "http://localhost:8080";
    private final String REQUEST_MAPPING_URI = "/trainer";

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private TrainerService service;

    @Autowired
    private TraineeService traineeService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrainingTypeRepository trainingTypeRepository;

    @Test
    public void createTrainerTest() throws JsonProcessingException {
        //Arrange
        CreateTrainerRequest request = new CreateTrainerRequest(
                "Daniel",
                "Benavides",
                "zumba"
        );
        RestAssured.baseURI = BASE_URI + REQUEST_MAPPING_URI;
        RequestSpecification httpRequest = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(request);
        //Act
        SignupResponse signupResponse = mapper.readValue(httpRequest.post().asString(), SignupResponse.class);
        //Assert
        assertNotNull(service.getTrainerByUsername(signupResponse.username));
    }

    @Test
    public void updateTrainerTest() throws JsonProcessingException {
        //Arrange
        User user = new User("Daniel","Benavides",true, userRepository);
        Trainer createdTrainer = service.createTrainer(
                new Trainer(trainingTypeRepository.getByName("resistance"), user)
        );
        UpdateTrainerRequest request = new UpdateTrainerRequest(
                createdTrainer.getUser().getUsername(),
                "DanielModified",
                "BenavidesModified",
                true,
                "fitness"
        );
        RestAssured.baseURI = BASE_URI + REQUEST_MAPPING_URI;
        RequestSpecification httpRequest = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(request);
        //Act
        TrainerProfileResponse response = mapper.readValue(httpRequest.put().asString(), TrainerProfileResponse.class);
        //Assert
        Trainer actualTrainee = service.getTrainerByUsername(createdTrainer.getUser().getUsername());
        assertEquals(request.specialization, actualTrainee.getSpecialization().getName());
        assertEquals(request.firstName, actualTrainee.getUser().getFirstName());
        assertEquals(request.lastName, actualTrainee.getUser().getLastName());
    }

    @Test
    public void getTrainerTest() throws JsonProcessingException {
        //Arrange
        User user = new User("Daniel","Benavides",true, userRepository);
        Trainer createdTrainer = service.createTrainer(
                new Trainer(trainingTypeRepository.getByName("resistance"), user)
        );
        RestAssured.baseURI = BASE_URI + REQUEST_MAPPING_URI+ "/" + createdTrainer.getUser().getUsername();
        RequestSpecification httpRequest = RestAssured.given()
                .header("Content-Type", "application/json");
        //Act
        System.out.println(httpRequest.get().asString());
        TrainerProfileResponse response = mapper.readValue(httpRequest.get().asString(), TrainerProfileResponse.class);
        //Assert
        assertEquals(createdTrainer.getUser().getUsername(), response.username);
    }
}
