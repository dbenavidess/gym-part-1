package com.dbenavidess.gym_part_1.integrationTest.steps;

import com.dbenavidess.gym_part_1.config.security.service.JwtService;
import com.dbenavidess.gym_part_1.domain.model.Trainer;
import com.dbenavidess.gym_part_1.domain.model.TrainingType;
import com.dbenavidess.gym_part_1.domain.model.User;
import com.dbenavidess.gym_part_1.domain.repository.TrainingTypeRepository;
import com.dbenavidess.gym_part_1.domain.repository.UserRepository;
import com.dbenavidess.gym_part_1.domain.util.PasswordEncryptionProvider;
import com.dbenavidess.gym_part_1.infrastructure.request.trainer.CreateTrainerRequest;
import com.dbenavidess.gym_part_1.infrastructure.request.trainer.UpdateTrainerRequest;
import com.dbenavidess.gym_part_1.service.TrainerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class TrainerStepDefinitions {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private TrainerService trainerService;
    @Autowired private JwtService jwtService;
    @Autowired private TrainingTypeRepository trainingTypeRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncryptionProvider passwordEncryptionProvider;

    private final Map<String, TrainingType> trainingTypes = new HashMap<>();

    @Autowired
    private ScenarioContext scenarioContext;

    @Before
    public void setUp() {
        Mockito.reset(trainerService, trainingTypeRepository, userRepository, passwordEncryptionProvider, jwtService);
        trainingTypes.clear();
    }

    @Given("training type {string} exists")
    public void trainingTypeExists(String name) {
        TrainingType type = new TrainingType(UUID.randomUUID(), name);
        trainingTypes.put(name, type);
        when(trainingTypeRepository.getByName(name)).thenReturn(type);
    }

    @Given("username generated for {string} {string} is available")
    public void usernameAvailable(String firstName, String lastName) {
        String base = firstName + "." + lastName;
        when(userRepository.searchUsernameLike(base)).thenReturn(Collections.emptyList());
    }

    @Given("password encoder returns {string}")
    public void passwordEncoderReturns(String encodedPassword) {
        when(passwordEncryptionProvider.encode(anyString())).thenReturn(encodedPassword);
    }

    @Given("trainer creation returns the new trainer")
    public void trainerCreationReturnsTheNewTrainer() {
        when(trainerService.createTrainer(any(Trainer.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Given("jwt token generated is {string}")
    public void jwtTokenGeneratedIs(String token) {
        when(jwtService.generateToken(anyMap(), any())).thenReturn(token);
    }

    @Given("trainer creation fails with message {string}")
    public void trainerCreationFailsWithMessage(String message) {
        when(trainerService.createTrainer(any(Trainer.class)))
                .thenThrow(new IllegalArgumentException(message));
    }

    @Given("trainer update returns trainer with username {string} and specialization {string}")
    public void trainerUpdateReturns(String username, String specializationName) {
        TrainingType type = trainingTypes.computeIfAbsent(
                specializationName, name -> new TrainingType(UUID.randomUUID(), name));
        User user = new User(UUID.randomUUID(), "John", "Doe", username, "encodedPassword", true);
        Trainer trainer = new Trainer(UUID.randomUUID(), user, type);

        when(trainerService.updateTrainer(any(Trainer.class))).thenReturn(trainer);
    }

    @Given("trainer has no trainees")
    public void trainerHasNoTrainees() {
        when(trainerService.getTrainees(any(Trainer.class))).thenReturn(Collections.emptyList());
    }

    @Given("trainer lookup for {string} returns specialization {string}")
    public void trainerLookupReturns(String username, String specializationName) {
        TrainingType type = trainingTypes.computeIfAbsent(
                specializationName, name -> new TrainingType(UUID.randomUUID(), name));
        User user = new User(UUID.randomUUID(), "John", "Doe", username, "encodedPassword", true);
        Trainer trainer = new Trainer(UUID.randomUUID(), user, type);

        when(trainerService.getTrainerByUsername(username)).thenReturn(trainer);
        when(trainerService.getTrainees(argThat(t ->
                t != null && username.equals(t.getUser().getUsername())
        ))).thenReturn(Collections.emptyList());
    }

    @Given("trainer lookup for {string} returns nothing")
    public void trainerLookupReturnsNothing(String username) {
        when(trainerService.getTrainerByUsername(username)).thenReturn(null);
    }

    @When("the client POSTs to {string} with first name {string}, last name {string} and specialization {string}")
    public void postCreateTrainer(String path, String firstName, String lastName, String specialization) throws Exception {
        CreateTrainerRequest request = new CreateTrainerRequest(firstName, lastName, specialization);

        MvcResult mvcResult = mockMvc.perform(
                        post(path)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andReturn();
        scenarioContext.setMvcResult(mvcResult);
    }

    @When("the client PUTs to {string} with username {string}, first name {string}, last name {string}, specialization {string} and active {string}")
    public void putUpdateTrainer(String path, String username, String firstName, String lastName,
                                 String specialization, String active) throws Exception {
        UpdateTrainerRequest request = new UpdateTrainerRequest(username,firstName,lastName,Boolean.parseBoolean(active),specialization);

        MvcResult mvcResult = mockMvc.perform(
                        put(path)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andReturn();
        scenarioContext.setMvcResult(mvcResult);
    }

    @When("the client GETs {string} trainer")
    public void getTrainer(String path) throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(path)).andReturn();
        scenarioContext.setMvcResult(mvcResult);
    }

    @Then("trainer creation is requested")
    public void trainerCreationIsRequested() {
        verify(trainerService).createTrainer(any(Trainer.class));
    }


    @Then("trainer trainees are not requested")
    public void trainerTraineesAreNotRequested() {
        verify(trainerService, never()).getTrainees(any(Trainer.class));
    }


}
