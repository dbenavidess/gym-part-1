package com.dbenavidess.gym_part_1.controller;

import com.dbenavidess.gym_part_1.service.TraineeService;
import com.dbenavidess.gym_part_1.domain.model.Trainee;
import com.dbenavidess.gym_part_1.domain.model.User;
import com.dbenavidess.gym_part_1.domain.repository.UserRepository;
import com.dbenavidess.gym_part_1.infrastructure.request.Login.ChangePasswordRequest;
import com.dbenavidess.gym_part_1.infrastructure.request.Login.LoginRequest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LoginControllerTest {

    private final String BASE_URI = "http://localhost:8080";



    @Autowired
    private TraineeService service;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void LoginTest(){
        //Arrange
        User user = new User("Juan","Lopez",true, userRepository);
        Trainee createdTrainee = service.createTrainee(new Trainee("Juan's address", Date.valueOf("1990-08-10"), user));
        LoginRequest request = new LoginRequest(
                createdTrainee.getUser().getUsername(),
                createdTrainee.getUser().getPassword()
        );
        RestAssured.baseURI = BASE_URI + "/login";
        RequestSpecification httpRequest = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(request);
        //Act
        Response response = httpRequest.post();
        //Assert
        assertEquals(200,response.statusCode());

    }

    @Test
    public void ChangePasswordTest(){
        //Arrange
        User user = new User("Juan","Lopez",true, userRepository);
        Trainee createdTrainee = service.createTrainee(new Trainee("Juan's address", Date.valueOf("1990-08-10"), user));
        ChangePasswordRequest request = new ChangePasswordRequest(
                createdTrainee.getUser().getUsername(),
                createdTrainee.getUser().getPassword(),
                "NewPassword"
        );
        RestAssured.baseURI = BASE_URI + "/change-password";
        RequestSpecification httpRequest = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(request);
        //Act
        Response response = httpRequest.put();
        //Assert
        assertEquals(200,response.statusCode());
    }

    @Test
    public void ChangeIsActiveTest(){
        //Arrange
        User user = new User("Juan","Lopez",true, userRepository);
        Trainee createdTrainee = service.createTrainee(new Trainee("Juan's address", Date.valueOf("1990-08-10"), user));

        RestAssured.baseURI = BASE_URI + "/change-active";
        RequestSpecification httpRequest = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(createdTrainee.getUser().getUsername());
        //Act
        Response response = httpRequest.patch();
        //Assert
        assertEquals(200,response.statusCode());
    }
}
