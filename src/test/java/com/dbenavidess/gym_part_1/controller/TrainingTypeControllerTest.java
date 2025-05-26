package com.dbenavidess.gym_part_1.controller;

import com.dbenavidess.gym_part_1.domain.model.TrainingType;
import com.dbenavidess.gym_part_1.domain.repository.TrainingTypeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TrainingTypeControllerTest {
    private final String BASE_URI = "http://localhost:8080";
    private final String REQUEST_MAPPING_URI = "/training-types";

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void getAllTrainingTypes() throws JsonProcessingException {
        //Arrange
        RestAssured.baseURI = BASE_URI + REQUEST_MAPPING_URI;
        RequestSpecification httpRequest = RestAssured.given()
                .header("Content-Type", "application/json");
        //Act
        List<TrainingType> response = mapper.readValue(httpRequest.get().asString(), new TypeReference<List<TrainingType>>(){} );
        //Assert
        assertEquals(5,response.size());


    }
}
