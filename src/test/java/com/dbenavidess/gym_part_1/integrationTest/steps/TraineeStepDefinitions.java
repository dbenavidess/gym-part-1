package com.dbenavidess.gym_part_1.integrationTest.steps;

import com.dbenavidess.gym_part_1.config.security.service.JwtService;
import com.dbenavidess.gym_part_1.domain.model.Trainee;
import com.dbenavidess.gym_part_1.domain.model.Trainer;
import com.dbenavidess.gym_part_1.domain.model.TrainingType;
import com.dbenavidess.gym_part_1.domain.model.User;
import com.dbenavidess.gym_part_1.domain.repository.UserRepository;
import com.dbenavidess.gym_part_1.domain.util.PasswordEncryptionProvider;
import com.dbenavidess.gym_part_1.infrastructure.request.trainee.CreateTraineeRequest;
import com.dbenavidess.gym_part_1.infrastructure.request.trainee.UpdateTraineeRequest;
import com.dbenavidess.gym_part_1.service.TraineeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class TraineeStepDefinitions{

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private TraineeService traineeService;
    @Autowired private JwtService jwtService;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncryptionProvider passwordEncryptionProvider;

    private final Map<String, Trainee> traineesByUsername = new HashMap<>();

    @Autowired
    private ScenarioContext scenarioContext;

    @Before
    public void setUp() {
        Mockito.reset(traineeService, userRepository, passwordEncryptionProvider, jwtService);
        traineesByUsername.clear();
    }

    /* ---------- Givens ---------- */

    @Given("trainee creation returns the new trainee")
    public void traineeCreationReturnsTheNewTrainee() {
        when(traineeService.createTrainee(any(Trainee.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Given("trainee creation fails with message {string}")
    public void traineeCreationFailsWithMessage(String message) {
        when(traineeService.createTrainee(any(Trainee.class)))
                .thenThrow(new IllegalArgumentException(message));
    }

    @Given("trainee update returns trainee with username {string}, first name {string}, last name {string}, address {string}, birth date {string} and active {string}")
    public void traineeUpdateReturns(String username, String firstName, String lastName,
                                     String address, String birthDate, String active) {
        User user = new User(UUID.randomUUID(), firstName, lastName, username, "encodedPassword", Boolean.parseBoolean(active));
        Trainee trainee = new Trainee(UUID.randomUUID(), address, Date.valueOf(birthDate), user);

        when(traineeService.updateTrainee(any(Trainee.class))).thenReturn(trainee);
        when(traineeService.getTrainerList(any(Trainee.class))).thenReturn(Collections.emptyList());
    }

    @Given("trainee lookup for {string} returns first name {string}, last name {string}, address {string}, birth date {string} and active {string}")
    public void traineeLookupReturns(String username, String firstName, String lastName,
                                     String address, String birthDate, String active) {
        User user = new User(UUID.randomUUID(), firstName, lastName, username, "encodedPassword", Boolean.parseBoolean(active));
        Trainee trainee = new Trainee(UUID.randomUUID(), address, Date.valueOf(birthDate), user);

        traineesByUsername.put(username, trainee);
        when(traineeService.getTraineeByUsername(username)).thenReturn(trainee);
    }

    @Given("trainee lookup for {string} returns nothing")
    public void traineeLookupReturnsNothing(String username) {
        when(traineeService.getTraineeByUsername(username)).thenReturn(null);
    }

    @Given("trainee trainer list for {string} returns trainers {string}")
    public void traineeTrainerListReturns(String username, String trainersCsv) {
        Trainee trainee = ensureTrainee(username);
        List<Trainer> trainers = buildTrainerList(trainersCsv);
        when(traineeService.getTrainerList(eq(trainee))).thenReturn(trainers);
    }

    @Given("not assigned trainers for {string} returns trainers {string}")
    public void notAssignedTrainersReturns(String username, String trainersCsv) {
        Trainee trainee = ensureTrainee(username);
        List<Trainer> trainers = buildTrainerList(trainersCsv);
        when(traineeService.getNotAssignedTrainerList(eq(trainee))).thenReturn(trainers);
    }

    @Given("updating trainee {string} trainers succeeds returning trainers {string}")
    public void updatingTraineeTrainersSucceeds(String username, String trainersCsv) {
        List<Trainer> trainers = buildTrainerList(trainersCsv);
        when(traineeService.updateTraineeTrainerList(anyList(), eq(username))).thenReturn(trainers);
    }

    @Given("updating trainee {string} trainers fails with no such element")
    public void updatingTraineeTrainersFails(String username) {
        when(traineeService.updateTraineeTrainerList(anyList(), eq(username)))
                .thenThrow(new NoSuchElementException("Trainee not found"));
    }

    /* ---------- When ---------- */

    @When("the client POSTs to {string} with first name {string}, last name {string}, address {string} and birth date {string}")
    public void postCreateTrainee(String path, String firstName, String lastName,
                                  String address, String birthDate) throws Exception {
        CreateTraineeRequest request = new CreateTraineeRequest(firstName,lastName,Date.valueOf(birthDate),address);
        MvcResult mvcResult = mockMvc.perform(
                        post(path)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andReturn();
        scenarioContext.setMvcResult(mvcResult);
    }

    @When("the client PUTs to {string} with username {string}, first name {string}, last name {string}, address {string}, birth date {string} and active {string}")
    public void putUpdateTrainee(String path, String username, String firstName, String lastName,
                                 String address, String birthDate, String active) throws Exception {
        UpdateTraineeRequest request = new UpdateTraineeRequest(username,firstName,lastName,Date.valueOf(birthDate),address,Boolean.parseBoolean(active));

        MvcResult mvcResult = mockMvc.perform(
                        put(path)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andReturn();
        scenarioContext.setMvcResult(mvcResult);
    }

    @When("the client GETs {string}")
    public void getTrainee(String path) throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(path)).andReturn();
        scenarioContext.setMvcResult(mvcResult);
    }

    @When("the client DELETEs {string} trainee")
    public void deleteTrainee(String path) throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete(path)).andReturn();
        scenarioContext.setMvcResult(mvcResult);
    }

    @When("the client PUTs to {string} with trainer usernames {string}")
    public void putUpdateTraineeTrainers(String path, String trainersCsv) throws Exception {
        List<String> body = Arrays.stream(trainersCsv.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        MvcResult mvcResult = mockMvc.perform(
                        put(path)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(body)))
                .andReturn();
        scenarioContext.setMvcResult(mvcResult);
    }

    /* ---------- Then ---------- */

    @Then("trainee creation is requested")
    public void traineeCreationIsRequested() {
        verify(traineeService).createTrainee(any(Trainee.class));
    }

    @Then("trainee trainer list is requested")
    public void traineeTrainerListIsRequested() {
        verify(traineeService).getTrainerList(any(Trainee.class));
    }

    @Then("trainee trainer list is not requested")
    public void traineeTrainerListIsNotRequested() {
        verify(traineeService, never()).getTrainerList(any(Trainee.class));
    }

    @Then("trainee deletion is requested for {string}")
    public void traineeDeletionIsRequested(String username) {
        verify(traineeService).deleteByUsername(username);
    }

    @Then("update trainee trainer list is requested for {string}")
    public void updateTraineeTrainerListIsRequested(String username) {
        verify(traineeService).updateTraineeTrainerList(anyList(), eq(username));
    }


    /* ---------- Helpers ---------- */

    private Trainee ensureTrainee(String username) {
        return traineesByUsername.computeIfAbsent(username, key -> {
            User user = new User(UUID.randomUUID(), "First", "Last", key, "encodedPassword", true);
            return new Trainee(UUID.randomUUID(), "Default address", Date.valueOf("2000-01-01"), user);
        });
    }

    private List<Trainer> buildTrainerList(String trainersCsv) {
        if (trainersCsv == null || trainersCsv.isBlank()) {
            return Collections.emptyList();
        }
        return Arrays.stream(trainersCsv.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(this::trainerFromUsername)
                .collect(Collectors.toList());
    }

    private Trainer trainerFromUsername(String username) {
        String[] parts = username.split("\\.");
        String firstName = parts.length > 0 ? capitalize(parts[0]) : "Trainer";
        String lastName = parts.length > 1 ? capitalize(parts[1]) : "User";

        User user = new User(UUID.randomUUID(), firstName, lastName, username, "encodedPassword", true);
        TrainingType specialization = new TrainingType(UUID.randomUUID(), parts.length > 1 ? parts[1] : "General");
        return new Trainer(UUID.randomUUID(), user, specialization);
    }

    private String capitalize(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}