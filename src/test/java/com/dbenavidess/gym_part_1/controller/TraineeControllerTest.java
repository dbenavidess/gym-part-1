package com.dbenavidess.gym_part_1.controller;

import com.dbenavidess.gym_part_1.domain.util.PasswordEncryptionProvider;
import com.dbenavidess.gym_part_1.infrastructure.request.Login.LoginRequest;
import com.dbenavidess.gym_part_1.infrastructure.response.LoginResponse;
import com.dbenavidess.gym_part_1.service.TraineeService;
import com.dbenavidess.gym_part_1.service.TrainerService;
import com.dbenavidess.gym_part_1.domain.model.Trainee;
import com.dbenavidess.gym_part_1.domain.model.Trainer;
import com.dbenavidess.gym_part_1.domain.model.User;
import com.dbenavidess.gym_part_1.domain.repository.TrainingTypeRepository;
import com.dbenavidess.gym_part_1.domain.repository.UserRepository;
import com.dbenavidess.gym_part_1.infrastructure.request.Trainee.CreateTraineeRequest;
import com.dbenavidess.gym_part_1.infrastructure.request.Trainee.UpdateTraineeRequest;
import com.dbenavidess.gym_part_1.infrastructure.response.SignupResponse;
import com.dbenavidess.gym_part_1.infrastructure.response.TraineeProfileResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TraineeControllerTest {
    private final String BASE_URI = "http://localhost:8080";
    private final String REQUEST_MAPPING_URI = "/trainee";

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private TraineeService service;

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    TrainingTypeRepository trainingTypeRepository;

    @Autowired
    private PasswordEncryptionProvider passwordEncryptionProvider;

    User user;
    Trainee createdTrainee;
    User user2;
    Trainer createdTrainer;
    User user3;
    Trainer createdTrainer2;
    String authHeader;


    @BeforeEach
    public void createUsersAndLogin() throws JsonProcessingException {
        user = new User("Daniel","Benavides",true, userRepository, passwordEncryptionProvider);
        createdTrainee = service.createTrainee(new Trainee("my house", Date.valueOf("1997-07-19"), user));

        user2 = new User("John","Wick",true, userRepository, passwordEncryptionProvider);
        createdTrainer = trainerService.createTrainer(new Trainer(trainingTypeRepository.getByName("resistance"), user2));

        user3 = new User("Tony","Hawk",true, userRepository, passwordEncryptionProvider);
        createdTrainer2 = trainerService.createTrainer(new Trainer(trainingTypeRepository.getByName("fitness"), user3));

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
    public void createTraineeTest() throws JsonProcessingException {
        //Arrange
        CreateTraineeRequest request = new CreateTraineeRequest(
                "Daniel",
                "Benavides",
                Date.valueOf("1997-07-19"),
                "fake address for creation"
        );
        RestAssured.baseURI = BASE_URI + REQUEST_MAPPING_URI;
        RequestSpecification httpRequest = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(request);
        //Act
        SignupResponse signupResponse = mapper.readValue(httpRequest.post().asString(), SignupResponse.class);
        //Assert
        assertNotNull(service.getTraineeByUsername(signupResponse.username));
        assertNotNull(signupResponse.token);

    }

    @Test
    public void updateTraineeTest() throws JsonProcessingException {

        UpdateTraineeRequest request = new UpdateTraineeRequest(
                createdTrainee.getUser().getUsername(),
                "DanielModified",
                "BenavidesModified",
                Date.valueOf("1957-07-19"),
                "address but modified",
                createdTrainee.getUser().getIsActive()
        );
        RestAssured.baseURI = BASE_URI + REQUEST_MAPPING_URI;
        RequestSpecification httpRequest = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", authHeader)
                .body(request);
        //Act
        TraineeProfileResponse response = mapper.readValue(httpRequest.put().asString(), TraineeProfileResponse.class);

        //Assert
        Trainee actualTrainee = service.getTraineeByUsername(createdTrainee.getUser().getUsername());
        assertEquals(request.address, actualTrainee.getAddress());
        assertEquals(request.dateOfBirth, actualTrainee.getDateOfBirth());
        assertEquals(request.firstName, actualTrainee.getUser().getFirstName());
        assertEquals(request.lastName, actualTrainee.getUser().getLastName());
    }

    @Test
    public void getTraineeTest() throws JsonProcessingException {
        //Arrange
        RestAssured.baseURI = BASE_URI + REQUEST_MAPPING_URI + "/" + createdTrainee.getUser().getUsername();
        RequestSpecification httpRequest = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", authHeader);
        //Act
        TraineeProfileResponse response = mapper.readValue(httpRequest.get().asString(), TraineeProfileResponse.class);
        //Assert
        assertEquals(createdTrainee.getUser().getUsername(), response.username);

    }

    @Test
    public void getNotAssignedTrainersTest() throws JsonProcessingException {
        //Arrange
        List<Trainer> trainers = service.updateTraineeTrainerList(
                List.of(createdTrainer.getUser().getUsername()),
                createdTrainee.getUser().getUsername());

        RestAssured.baseURI = BASE_URI + REQUEST_MAPPING_URI + "/" + createdTrainee.getUser().getUsername() + "/get-not-assigned-trainers";
        RequestSpecification httpRequest = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", authHeader);
        //Act
        List<TraineeProfileResponse.TraineeTrainerRepr> response = mapper.readValue(httpRequest.get().asString(), new TypeReference<List<TraineeProfileResponse.TraineeTrainerRepr>>() {});

        //Assert
        assertFalse(response.isEmpty());
        assertTrue(response.stream().anyMatch(traineeTrainerRepr -> Objects.equals(traineeTrainerRepr.username, createdTrainer2.getUser().getUsername())));

    }

    @Test
    public void updateTraineeTrainersListTest() throws JsonProcessingException {
        //Arrange
        RestAssured.baseURI = BASE_URI + REQUEST_MAPPING_URI + "/" + createdTrainee.getUser().getUsername() + "/update-trainers";
        RequestSpecification httpRequest = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", authHeader)
                .body(List.of(createdTrainer.getUser().getUsername(),createdTrainer2.getUser().getUsername()));
        //Act
        List<TraineeProfileResponse.TraineeTrainerRepr> response = mapper.readValue(httpRequest.put().asString(), new TypeReference<>() {
        });
        //Assert
        assertEquals(2, response.size());

    }

    @Test
    public void deleteTrainee(){
        //Arrange
        RestAssured.baseURI = BASE_URI + REQUEST_MAPPING_URI + "/" + createdTrainee.getUser().getUsername();
        RequestSpecification httpRequest = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", authHeader);
        Response response = httpRequest.delete();
        //assert
        assertEquals( 200, response.statusCode());
    }

}
