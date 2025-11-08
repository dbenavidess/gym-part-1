package com.dbenavidess.gym_part_1.integrationTest.steps;

import com.dbenavidess.gym_part_1.config.security.service.JwtService;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class LogoutStepDefinitions {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private ScenarioContext scenarioContext;

    @Before
    public void setUp() {
        Mockito.reset(jwtService);
    }

    @Given("a token {string} belongs to user {string}")
    public void tokenBelongsToUser(String bearerToken, String username) {
        String token = bearerToken.replace("Bearer ", "");
        when(jwtService.extractUsername(token)).thenReturn(username);
    }

    @When("the client POSTs to {string} with header {string} set to {string}")
    public void postWithAuthorization(String path, String headerName, String headerValue) throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        post(path)
                                .header(headerName, headerValue)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        scenarioContext.setMvcResult(mvcResult);
    }

    @When("the client POSTs to {string} without an Authorization header")
    public void postWithoutAuthorization(String path) throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(path)).andReturn();
        scenarioContext.setMvcResult(mvcResult);
    }

    @Then("the token for user {string} is invalidated")
    public void tokenInvalidated(String username) {
        verify(jwtService).deleteByUsername(eq(username));
    }

}