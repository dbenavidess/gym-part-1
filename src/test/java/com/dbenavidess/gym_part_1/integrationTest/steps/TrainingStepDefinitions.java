package com.dbenavidess.gym_part_1.integrationTest.steps;

import com.dbenavidess.gym_part_1.domain.model.Trainee;
import com.dbenavidess.gym_part_1.domain.model.Trainer;
import com.dbenavidess.gym_part_1.domain.model.Training;
import com.dbenavidess.gym_part_1.domain.model.TrainingType;
import com.dbenavidess.gym_part_1.domain.model.User;
import com.dbenavidess.gym_part_1.service.TrainingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.sql.Date;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class TrainingStepDefinitions {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private TrainingService trainingService;

    private CreateParams lastCreateParams;
    private SearchParams lastSearchParams;

    @Autowired
    private ScenarioContext scenarioContext;

    private record CreateParams(String name, Date date, Integer duration,
                                String trainingTypeName, String trainerUsername, String traineeUsername) {}

    private record SearchParams(String trainerUsername, String trainingTypeName,
                                Date from, Date to, String traineeUsername) {}

    @Before
    public void setUp() {
        Mockito.reset(trainingService);
        lastCreateParams = null;
        lastSearchParams = null;
    }

    /* --------------------------- Given --------------------------- */

    @Given("training creation will succeed")
    public void trainingCreationWillSucceed() {
        when(trainingService.createTraining(anyString(), any(Date.class), any(), anyString(), anyString(), anyString()))
                .thenAnswer(invocation -> {
                    String name = invocation.getArgument(0);
                    Date date = invocation.getArgument(1);
                    Integer duration = invocation.getArgument(2);
                    String typeName = invocation.getArgument(3);
                    String trainerUsername = invocation.getArgument(4);
                    String traineeUsername = invocation.getArgument(5);

                    TrainingType type = new TrainingType(UUID.randomUUID(),
                            (typeName == null || typeName.isBlank()) ? "General" : typeName);

                    User trainerUser = new User(UUID.randomUUID(),
                            "TrainerFirst", "TrainerLast",
                            trainerUsername == null ? "trainer.default" : trainerUsername,
                            "hashed", true);
                    Trainer trainer = new Trainer(UUID.randomUUID(), trainerUser, type);

                    User traineeUser = new User(UUID.randomUUID(),
                            "TraineeFirst", "TraineeLast",
                            traineeUsername == null ? "trainee.default" : traineeUsername,
                            "hashed", true);
                    Trainee trainee = new Trainee(UUID.randomUUID(),
                            "123 Address", Date.valueOf("1990-01-01"), traineeUser);

                    return new Training(UUID.randomUUID(), trainer, trainee,
                            name, type, date == null ? Date.valueOf("2024-01-01") : date,
                            duration == null ? 30 : duration);
                });
    }

    @Given("training search will return 1 result")
    public void trainingSearchWillReturnOneResult() {
        TrainingType type = new TrainingType(UUID.randomUUID(), "Cardio");
        User trainerUser = new User(UUID.randomUUID(), "TrainerFirst", "TrainerLast", "trainer.john", "hashed", true);
        Trainer trainer = new Trainer(UUID.randomUUID(), trainerUser, type);
        User traineeUser = new User(UUID.randomUUID(), "TraineeFirst", "TraineeLast", "trainee.tom", "hashed", true);
        Trainee trainee = new Trainee(UUID.randomUUID(), "Main St", Date.valueOf("1995-05-05"), traineeUser);
        Training training = new Training(UUID.randomUUID(), trainer, trainee,
                "Morning Workout", type, Date.valueOf("2024-05-01"), 60);

        when(trainingService.searchTrainings(any(), any(), any(), any(), any()))
                .thenReturn(List.of(training));
    }

    @Given("deleting training with id {string} throws not found")
    public void deletingTrainingThrowsNotFound(String id) {
        UUID uuid = UUID.fromString(id);
        doThrow(new NoSuchElementException("Training not found"))
                .when(trainingService).deleteTraining(uuid);
    }

    /* --------------------------- When --------------------------- */

    @When("the client POSTs to {string} with name {string}, date {string}, duration {string}, training type {string}, trainer username {string} and trainee username {string}")
    public void postCreateTraining(String path, String name, String dateStr, String durationStr,
                                   String trainingTypeName, String trainerUsername, String traineeUsername) throws Exception {

        Date date = parseDate(dateStr);
        Integer duration = (durationStr == null || durationStr.isBlank()) ? null : Integer.valueOf(durationStr);

        lastCreateParams = new CreateParams(name, date, duration, trainingTypeName, trainerUsername, traineeUsername);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", name);
        requestBody.put("date", dateStr);            // keep raw string to avoid timezone shift
        requestBody.put("duration", duration);
        requestBody.put("trainingTypeName", trainingTypeName);
        requestBody.put("trainerUsername", trainerUsername);
        requestBody.put("traineeUsername", traineeUsername);

        MvcResult result = mockMvc.perform(
                        post(path)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestBody)))
                .andReturn();

        scenarioContext.setMvcResult(result);
    }

    @When("the client GETs {string} with trainer username {string}, training type {string}, from {string}, to {string}, trainee username {string}")
    public void getSearchTrainings(String path, String trainerUsername, String trainingTypeName,
                                   String fromStr, String toStr, String traineeUsername) throws Exception {

        Date from = parseDate(fromStr);
        Date to = parseDate(toStr);

        lastSearchParams = new SearchParams(
                emptyToNull(trainerUsername),
                emptyToNull(trainingTypeName),
                from,
                to,
                emptyToNull(traineeUsername)
        );

        var builder = get(path);
        if (trainerUsername != null && !trainerUsername.isBlank()) {
            builder.param("trainerUsername", trainerUsername);
        }
        if (trainingTypeName != null && !trainingTypeName.isBlank()) {
            builder.param("trainingTypeName", trainingTypeName);
        }
        if (from != null) {
            builder.param("from", from.toString());
        }
        if (to != null) {
            builder.param("to", to.toString());
        }

        if (traineeUsername != null && !traineeUsername.isBlank()) {
            builder.param("traineeUsername", traineeUsername);
        }

        MvcResult mvcResult = mockMvc.perform(builder).andReturn();
        scenarioContext.setMvcResult(mvcResult);
    }

    @When("the client DELETEs {string} training")
    public void deleteTraining(String path) throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete(path)).andReturn();
        scenarioContext.setMvcResult(mvcResult);
    }

    /* --------------------------- Then --------------------------- */


    @Then("training creation is invoked with name {string}, date {string}, duration {string}, training type {string}, trainer username {string} and trainee username {string}")
    public void trainingCreationIsInvoked(String name, String dateStr, String durationStr,
                                          String trainingTypeName, String trainerUsername, String traineeUsername) {
        Integer expectedDuration = durationStr == null || durationStr.isBlank() ? null : Integer.valueOf(durationStr);
        ArgumentCaptor<Date> dateCaptor = ArgumentCaptor.forClass(Date.class);

        verify(trainingService).createTraining(
                eq(name),
                dateCaptor.capture(),
                eq(expectedDuration),
                eq(trainingTypeName),
                eq(trainerUsername),
                eq(traineeUsername)
        );

        assertThat(dateCaptor.getValue()).isNotNull();
    }

    @Then("training search is invoked with trainer username {string}, training type {string}, from {string}, to {string}, trainee username {string}")
    public void trainingSearchIsInvoked(String trainerUsername, String trainingTypeName,
                                        String fromStr, String toStr, String traineeUsername) {
        Date expectedFrom = parseDate(fromStr);
        Date expectedTo = parseDate(toStr);

        verify(trainingService).searchTrainings(
                emptyToNull(trainerUsername),
                expectedFrom,
                expectedTo,
                emptyToNull(traineeUsername),
                emptyToNull(trainingTypeName)
        );
    }

    @Then("training search is not invoked")
    public void trainingSearchIsNotInvoked() {
        verify(trainingService, never()).searchTrainings(any(), any(), any(), any(), any());
    }

    @Then("training deletion is invoked for id {string}")
    public void trainingDeletionIsInvoked(String id) {
        verify(trainingService).deleteTraining(UUID.fromString(id));
    }

    @Then("the response JSON array size is {int}")
    public void responseJsonArraySize(int expectedSize) throws Exception {
        int actualSize = JsonPath.read(scenarioContext.getMvcResult().getResponse().getContentAsString(), "$.length()");
        assertThat(actualSize).isEqualTo(expectedSize);
    }

    /* --------------------------- Helpers --------------------------- */

    private Date parseDate(String value) {
        return (value == null || value.isBlank()) ? null : Date.valueOf(value);
    }

    private String emptyToNull(String value) {
        return (value == null || value.isBlank()) ? null : value;
    }
}
