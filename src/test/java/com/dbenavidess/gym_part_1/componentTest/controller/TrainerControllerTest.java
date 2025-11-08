package com.dbenavidess.gym_part_1.componentTest.controller;

import com.dbenavidess.gym_part_1.domain.util.PasswordEncryptionProvider;
import com.dbenavidess.gym_part_1.infrastructure.request.login.LoginRequest;
import com.dbenavidess.gym_part_1.infrastructure.response.LoginResponse;
import com.dbenavidess.gym_part_1.service.TrainerService;
import com.dbenavidess.gym_part_1.domain.model.Trainer;
import com.dbenavidess.gym_part_1.domain.model.User;
import com.dbenavidess.gym_part_1.domain.repository.TrainingTypeRepository;
import com.dbenavidess.gym_part_1.domain.repository.UserRepository;
import com.dbenavidess.gym_part_1.infrastructure.request.trainer.CreateTrainerRequest;
import com.dbenavidess.gym_part_1.infrastructure.request.trainer.UpdateTrainerRequest;
import com.dbenavidess.gym_part_1.infrastructure.response.SignupResponse;
import com.dbenavidess.gym_part_1.infrastructure.response.TrainerProfileResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
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
    private UserRepository userRepository;

    @Autowired
    private TrainingTypeRepository trainingTypeRepository;

    @Autowired
    private PasswordEncryptionProvider passwordEncryptionProvider;

    private Trainer createdTrainer;
    private String authHeader;

    private String login(String username, String password) throws JsonProcessingException {
        LoginRequest request = new LoginRequest(
                username,
                password
        );
        RestAssured.baseURI = BASE_URI + "/login";
        RequestSpecification httpRequest = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(request);
        LoginResponse response = mapper.readValue(httpRequest.post().asString(), LoginResponse.class);
        return "Bearer " + response.jwtToken;
    }

    @BeforeEach
    public void createUsersAndLogin() throws JsonProcessingException {
        User user = new User("Daniel", "Benavides", true, userRepository, passwordEncryptionProvider);
        createdTrainer = service.createTrainer(
                new Trainer(trainingTypeRepository.getByName("resistance"), user)
        );
        authHeader = login(
                createdTrainer.getUser().getUsername(),
                user.getPlainPassword());
    }



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
                .header("Authorization",authHeader)
                .body(request);
        //Act
        TrainerProfileResponse response = mapper.readValue(httpRequest.put().asString(), TrainerProfileResponse.class);
        //Assert
        Trainer actualTrainee = service.getTrainerByUsername(createdTrainer.getUser().getUsername());
        assertNotNull(response);
        assertEquals(request.specialization, actualTrainee.getSpecialization().getName());
        assertEquals(request.firstName, actualTrainee.getUser().getFirstName());
        assertEquals(request.lastName, actualTrainee.getUser().getLastName());
    }

    @Test
    public void getTrainerTest() throws JsonProcessingException {
        //Arrange
        RestAssured.baseURI = BASE_URI + REQUEST_MAPPING_URI+ "/" + createdTrainer.getUser().getUsername();
        RequestSpecification httpRequest = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization",authHeader);
        //Act
        TrainerProfileResponse response = mapper.readValue(httpRequest.get().asString(), TrainerProfileResponse.class);
        //Assert
        assertEquals(createdTrainer.getUser().getUsername(), response.username);
    }
}
