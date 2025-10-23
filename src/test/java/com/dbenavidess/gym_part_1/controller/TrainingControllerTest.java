package com.dbenavidess.gym_part_1.controller;

import com.dbenavidess.gym_part_1.domain.util.PasswordEncryptionProvider;
import com.dbenavidess.gym_part_1.infrastructure.request.login.LoginRequest;
import com.dbenavidess.gym_part_1.infrastructure.response.LoginResponse;
import com.dbenavidess.gym_part_1.service.TraineeService;
import com.dbenavidess.gym_part_1.service.TrainerService;
import com.dbenavidess.gym_part_1.service.TrainingService;
import com.dbenavidess.gym_part_1.domain.model.Trainee;
import com.dbenavidess.gym_part_1.domain.model.Trainer;
import com.dbenavidess.gym_part_1.domain.model.Training;
import com.dbenavidess.gym_part_1.domain.model.User;
import com.dbenavidess.gym_part_1.domain.repository.TrainingTypeRepository;
import com.dbenavidess.gym_part_1.domain.repository.UserRepository;
import com.dbenavidess.gym_part_1.infrastructure.request.training.CreateTrainingRequest;
import com.dbenavidess.gym_part_1.infrastructure.response.TrainingDetailsResponse;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TrainingControllerTest {

    private final String BASE_URI = "http://localhost:8080";
    private final String REQUEST_MAPPING_URI = "/training";

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private TrainingService service;

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private TraineeService traineeService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrainingTypeRepository trainingTypeRepository;

    @Autowired
    private PasswordEncryptionProvider passwordEncryptionProvider;

    private User user;
    private Trainer createdTrainer;
    private User user2;
    private Trainee createdTrainee;
    private String authHeader;

    @BeforeEach
    public void createUsersAndLogin() throws JsonProcessingException{
        user = new User("Daniel","Benavides",true, userRepository, passwordEncryptionProvider);
        createdTrainer = trainerService.createTrainer(new Trainer(trainingTypeRepository.getByName("zumba"), user));
        user2 = new User("Juan","Lopez",true, userRepository, passwordEncryptionProvider);
        createdTrainee = traineeService.createTrainee(new Trainee("Juan's address", Date.valueOf("1990-08-10"), user2));

        authHeader = login(
                createdTrainer.getUser().getUsername(),
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
    public void createTrainingTest() {
        // Arrange
        CreateTrainingRequest request = new CreateTrainingRequest(
                createdTrainer.getUser().getUsername(),
                createdTrainee.getUser().getUsername(),
                "newTraining",
                "resistance",
                Date.valueOf("2025-05-25"),
                120
        );

        RestAssured.baseURI = BASE_URI + REQUEST_MAPPING_URI;
        RequestSpecification httpRequest = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization",authHeader)
                .body(request);
        //Act
        Response response = httpRequest.post();
        //Assert
        assertEquals(200,response.statusCode());

    }

    @Test
    public void searchTrainings() throws JsonProcessingException {
        // Arrange
        User user = new User("Daniel","Benavides",true, userRepository, passwordEncryptionProvider);
        Trainer createdTrainer = trainerService.createTrainer(new Trainer(trainingTypeRepository.getByName("zumba"), user));

        User user2 = new User("Juan","Lopez",true, userRepository, passwordEncryptionProvider);
        Trainee createdTrainee = traineeService.createTrainee(new Trainee("Juan's address", Date.valueOf("1990-08-10"), user2));

        Training createdTraining = service.createTraining(
                "zumba class",
                Date.valueOf("2025-04-21"),
                60,
                "zumba",
                createdTrainer.getUser().getUsername(),
                createdTrainee.getUser().getUsername()
        );
        Training createdTraining2 = service.createTraining(
                "zumba class",
                Date.valueOf("2025-04-19"),
                60,
                "zumba",
                createdTrainer.getUser().getUsername(),
                createdTrainee.getUser().getUsername()
        );
        Training createdTraining3 = service.createTraining(
                "fitness class",
                Date.valueOf("2025-05-01"),
                60,
                "fitness",
                createdTrainer.getUser().getUsername(),
                createdTrainee.getUser().getUsername()
        );
        RestAssured.baseURI = BASE_URI + REQUEST_MAPPING_URI;
        Map<String,String> params = new HashMap<>();
        params.put("trainerUsername",createdTrainer.getUser().getUsername());
        params.put("trainingTypeName","zumba");

        RequestSpecification httpRequest = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization",authHeader)
                .params(params);
        //Act
        System.out.println(httpRequest.get().asString());
        List<TrainingDetailsResponse> response = mapper.readValue(httpRequest.get().asString(),
                new TypeReference<List<TrainingDetailsResponse>>() {} );
        //Assert
        assertEquals(2,response.size());

    }

}