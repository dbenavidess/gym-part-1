package com.dbenavidess.gym_part_1.integrationTest.steps;

import com.dbenavidess.gym_part_1.config.security.service.BruteForceSecurityService;
import com.dbenavidess.gym_part_1.config.security.service.JwtService;
import com.dbenavidess.gym_part_1.domain.model.User;
import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.entitites.BruteForceSecurityEntity;
import com.dbenavidess.gym_part_1.infrastructure.request.login.LoginRequest;

import com.dbenavidess.gym_part_1.service.UserService;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.sql.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class LoginStepDefinitions {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private BruteForceSecurityService bruteForceSecurityService;


    @Autowired
    private ScenarioContext scenarioContext;

    @Before
    public void setUp() {
        Mockito.reset(userService, jwtService, bruteForceSecurityService);
    }

    @Given("there is a brute force record for user {string} without a lock")
    public void thereIsABruteForceRecord(String username) {
        BruteForceSecurityEntity entity = new BruteForceSecurityEntity();
        entity.setId(1L);
        entity.setFailedAttemptsCounter(1);
        entity.setUnlockDate(null);
        entity.setUser(null); // Not required for controller interaction

        when(bruteForceSecurityService.getByUsername(username)).thenReturn(entity);
        when(bruteForceSecurityService.clearFailedAttempts(any(BruteForceSecurityEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Given("user {string} is currently locked until one hour in the future")
    public void userIsLocked(String username) {
        BruteForceSecurityEntity entity = new BruteForceSecurityEntity();
        entity.setId(2L);
        entity.setFailedAttemptsCounter(3);
        entity.setUnlockDate(new Date(System.currentTimeMillis() + 3600_000));
        entity.setUser(null);

        when(bruteForceSecurityService.getByUsername(username)).thenReturn(entity);
    }

    @Given("the credentials for user {string} are valid and produce token {string}")
    public void credentialsValid(String username, String token) {
        when(userService.login(eq(username), eq("password"), any())).thenReturn(token);
    }

    @Given("the credentials for user {string} are rejected")
    public void credentialsRejected(String username) {
        when(userService.login(eq(username), anyString(), any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        User domainUser = new User(
                UUID.randomUUID(),
                "John",
                "Doe",
                username,
                "hashed-password",
                true
        );

        when(userService.getByUsername(username)).thenReturn(domainUser);
    }

    @When("the client POSTs to {string} with username {string} and password {string}")
    public void postLogin(String path, String username, String password) throws Exception {
        LoginRequest body = new LoginRequest(username,password);

        MvcResult mvcResult = mockMvc.perform(
                        post(path)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(body)))
                .andReturn();
        scenarioContext.setMvcResult(mvcResult);
    }

    @Then("the response body contains a JWT token {string}")
    public void responseContainsToken(String expectedToken) throws Exception {
        assertThat(scenarioContext.getMvcResult().getResponse().getContentAsString()).contains(expectedToken);
    }

    @Then("brute force attempts are cleared for user {string}")
    public void bruteForceCleared(String username) {
        verify(bruteForceSecurityService).clearFailedAttempts(argThat(entity ->
                entity.getUnlockDate() == null && entity.getFailedAttemptsCounter() >= 0));
    }

    @Then("the login service is not invoked for user {string}")
    public void loginServiceNotInvoked(String username) {
        verify(userService, never()).login(eq(username), anyString(), any());
    }

    @Then("a failed login attempt is recorded for user {string}")
    public void failedAttemptRecorded(String username) {
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(bruteForceSecurityService).addFailedAttempt(userCaptor.capture());
        assertThat(userCaptor.getValue().getUsername()).isEqualTo(username);
    }
}