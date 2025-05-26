package com.dbenavidess.gym_part_1.controller;

import com.dbenavidess.gym_part_1.application.service.TraineeService;
import com.dbenavidess.gym_part_1.application.service.TrainerService;
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

    @Test
    public void createTraineeTest() throws JsonProcessingException {
        //Arrange
        CreateTraineeRequest request = new CreateTraineeRequest(
                "Daniel",
                "Benavides",
                Date.valueOf("1997-07-19"),
                "fake address"
        );
        RestAssured.baseURI = BASE_URI + REQUEST_MAPPING_URI;
        RequestSpecification httpRequest = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(request);
        //Act
        SignupResponse signupResponse = mapper.readValue(httpRequest.post().asString(), SignupResponse.class);
        //Assert
        assertNotNull(service.getTraineeByUsername(signupResponse.username));

    }

    @Test
    public void updateTraineeTest() throws JsonProcessingException {
        //Arrange
        User user = new User("Daniel","Benavides",true, userRepository);
        Trainee createdTrainee = service.createTrainee(new Trainee("my house", Date.valueOf("1997-07-19"), user));

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
        User user = new User("Daniel","Benavides",true, userRepository);
        Trainee createdTrainee = service.createTrainee(new Trainee("my house", Date.valueOf("1997-07-19"), user));
        RestAssured.baseURI = BASE_URI + REQUEST_MAPPING_URI + "/" + createdTrainee.getUser().getUsername();
        RequestSpecification httpRequest = RestAssured.given()
                .header("Content-Type", "application/json");
        //Act
        TraineeProfileResponse response = mapper.readValue(httpRequest.get().asString(), TraineeProfileResponse.class);
        //Assert
        assertEquals(createdTrainee.getUser().getUsername(), response.username);

    }

    @Test
    public void getNotAssignedTrainersTest() throws JsonProcessingException {
        //Arrange
        User user = new User("Daniel","Benavides",true, userRepository);
        Trainee createdTrainee = service.createTrainee(new Trainee("my house", Date.valueOf("1997-07-19"), user));

        User user2 = new User("John","Wick",true, userRepository);
        Trainer createdTrainer = trainerService.createTrainer(new Trainer(trainingTypeRepository.getByName("resistance"), user2));

        User user3 = new User("Tony","Hawk",true, userRepository);
        Trainer createdTrainer2 = trainerService.createTrainer(new Trainer(trainingTypeRepository.getByName("fitness"), user3));

        List<Trainer> trainers = service.updateTraineeTrainerList(
                List.of(createdTrainer.getUser().getUsername()),
                createdTrainee.getUser().getUsername());

        RestAssured.baseURI = BASE_URI + REQUEST_MAPPING_URI + "/" + createdTrainee.getUser().getUsername() + "/get-not-assigned-trainers";
        RequestSpecification httpRequest = RestAssured.given()
                .header("Content-Type", "application/json");
        //Act
        List<TraineeProfileResponse.TraineeTrainerRepr> response = mapper.readValue(httpRequest.get().asString(), new TypeReference<List<TraineeProfileResponse.TraineeTrainerRepr>>() {});

        //Assert
        assertFalse(response.isEmpty());
        assertTrue(response.stream().anyMatch(traineeTrainerRepr -> Objects.equals(traineeTrainerRepr.username, createdTrainer2.getUser().getUsername())));

    }

    @Test
    public void updateTraineeTrainersListTest() throws JsonProcessingException {
        //Arrange
        User user = new User("Daniel","Benavides",true, userRepository);
        Trainee createdTrainee = service.createTrainee(new Trainee("my house", Date.valueOf("1997-07-19"), user));

        User user2 = new User("John","Wick",true, userRepository);
        Trainer createdTrainer = trainerService.createTrainer(new Trainer(trainingTypeRepository.getByName("resistance"), user2));

        User user3 = new User("Tony","Hawk",true, userRepository);
        Trainer createdTrainer2 = trainerService.createTrainer(new Trainer(trainingTypeRepository.getByName("fitness"), user3));

        RestAssured.baseURI = BASE_URI + REQUEST_MAPPING_URI + "/" + createdTrainee.getUser().getUsername() + "/update-trainers";
        RequestSpecification httpRequest = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(List.of(createdTrainer.getUser().getUsername(),createdTrainer2.getUser().getUsername()));
        //Act
        List<TraineeProfileResponse.TraineeTrainerRepr> response = mapper.readValue(httpRequest.put().asString(), new TypeReference<List<TraineeProfileResponse.TraineeTrainerRepr>>() {});
        //Assert
        assertEquals(2, response.size());

    }

    @Test
    public void deleteTrainee(){
        //Arrange
        User user = new User("Daniel","Benavides",true, userRepository);
        Trainee createdTrainee = service.createTrainee(new Trainee("my house", Date.valueOf("1997-07-19"), user));
        RestAssured.baseURI = BASE_URI + REQUEST_MAPPING_URI + "/" + createdTrainee.getUser().getUsername();
        RequestSpecification httpRequest = RestAssured.given()
                .header("Content-Type", "application/json");
        Response response = httpRequest.delete();

        assertEquals(response.statusCode(), 200);
    }

}
