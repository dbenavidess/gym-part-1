package com.dbenavidess.gym_part_1.controller;

import com.dbenavidess.gym_part_1.domain.model.Trainee;
import com.dbenavidess.gym_part_1.domain.model.TrainingType;
import com.dbenavidess.gym_part_1.domain.model.User;
import com.dbenavidess.gym_part_1.domain.repository.UserRepository;
import com.dbenavidess.gym_part_1.domain.util.PasswordEncryptionProvider;
import com.dbenavidess.gym_part_1.infrastructure.request.login.LoginRequest;
import com.dbenavidess.gym_part_1.infrastructure.response.LoginResponse;
import com.dbenavidess.gym_part_1.service.TraineeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TrainingTypeControllerTest {
    private final String BASE_URI = "http://localhost:8080";
    private final String REQUEST_MAPPING_URI = "/training-types";

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private TraineeService service;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncryptionProvider passwordEncryptionProvider;

    private User user;
    private Trainee createdTrainee;
    private String authHeader;

    @BeforeEach
    public void createUsersAndLogin() throws JsonProcessingException {
        user = new User("Juan","Lopez",true, userRepository, passwordEncryptionProvider);
        createdTrainee = service.createTrainee(new Trainee("Juan's address", Date.valueOf("1990-08-10"), user));
        authHeader = login(
                createdTrainee.getUser().getUsername(),
                user.getPlainPassword());
    }

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
    @Test
    public void getAllTrainingTypes() throws JsonProcessingException {
        //Arrange
        RestAssured.baseURI = BASE_URI + REQUEST_MAPPING_URI;
        RequestSpecification httpRequest = RestAssured.given()
                .header("Authorization", authHeader)
                .header("Content-Type", "application/json");
        //Act
        List<TrainingType> response = mapper.readValue(httpRequest.get().asString(), new TypeReference<>(){} );
        //Assert
        assertEquals(5,response.size());


    }
}
