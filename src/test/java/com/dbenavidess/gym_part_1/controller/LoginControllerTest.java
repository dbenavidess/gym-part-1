package com.dbenavidess.gym_part_1.controller;

import com.dbenavidess.gym_part_1.domain.util.PasswordEncryptionProvider;
import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.jpaRepositories.JwtRepository;
import com.dbenavidess.gym_part_1.infrastructure.response.LoginResponse;
import com.dbenavidess.gym_part_1.service.TraineeService;
import com.dbenavidess.gym_part_1.domain.model.Trainee;
import com.dbenavidess.gym_part_1.domain.model.User;
import com.dbenavidess.gym_part_1.domain.repository.UserRepository;
import com.dbenavidess.gym_part_1.infrastructure.request.login.ChangePasswordRequest;
import com.dbenavidess.gym_part_1.infrastructure.request.login.LoginRequest;
import com.dbenavidess.gym_part_1.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LoginControllerTest {

    private final String BASE_URI = "http://localhost:8080";

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private TraineeService service;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncryptionProvider passwordEncryptionProvider;

    @Autowired
    private JwtRepository jwtRepository;

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
    public void LoginTest() throws JsonProcessingException {
        //Arrange
        LoginRequest request = new LoginRequest(
                createdTrainee.getUser().getUsername(),
                user.getPlainPassword()
        );
        RestAssured.baseURI = BASE_URI + "/login";
        RequestSpecification httpRequest = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(request);
        //Act
        Response response = httpRequest.post();
        //Assert
        assertEquals(HttpStatus.OK.value(), response.statusCode());
    }

    @Test
    public void ChangePasswordTest(){
        //Arrange
        ChangePasswordRequest request = new ChangePasswordRequest(
                createdTrainee.getUser().getUsername(),
                createdTrainee.getUser().getPassword(),
                "NewPassword"
        );
        RestAssured.baseURI = BASE_URI + "/change-password";
        RequestSpecification httpRequest = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization",authHeader)
                .body(request);
        //Act
        Response response = httpRequest.put();
        //Assert
        assertEquals(200,response.statusCode());
    }

    @Test
    public void ChangeIsActiveTest(){
        //Arrange
        RestAssured.baseURI = BASE_URI + "/change-active";
        RequestSpecification httpRequest = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization",authHeader)
                .body(createdTrainee.getUser().getUsername());
        //Act
        Response response = httpRequest.patch();
        //Assert
        assertEquals(200,response.statusCode());
    }

    @Test
    public void forceSecurityTest() throws JsonProcessingException {
        Response response = null;
        for (int i = 0; i < 4; i++) {
            //Arrange
            LoginRequest request = new LoginRequest(
                    createdTrainee.getUser().getUsername(),
                    "ObjectivelyWrongPassword"
            );
            RestAssured.baseURI = BASE_URI + "/login";
            RequestSpecification httpRequest = RestAssured.given()
                    .header("Content-Type", "application/json")
                    .body(request);
            //Act
            response = httpRequest.post();
        }
        assertEquals(403, response.statusCode());
    }

    @Test
    public void logoutTest(){
        //Arrange
        RestAssured.baseURI = BASE_URI + "/logout";
        RequestSpecification httpRequest = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", authHeader);
        //Act
        Response response = httpRequest.post();
        //Assert
        assertEquals(200,response.statusCode());
        assertTrue(jwtRepository.findByUser_Username(createdTrainee.getUser().getUsername()).isEmpty());
    }

}
