package com.dbenavidess.gym_part_1.integrationTest.steps;



import com.dbenavidess.gym_part_1.infrastructure.request.login.ChangePasswordRequest;
import com.dbenavidess.gym_part_1.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

public class UserManagementStepDefinitions {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserService userService;


    @Autowired
    private ScenarioContext scenarioContext;

    @Before
    public void setUp() {
        Mockito.reset(userService);
    }

    @When("the client PUTs to {string} with username {string}, old password {string} and new password {string}")
    public void putChangePassword(String path, String username, String oldPassword, String newPassword) throws Exception {
        ChangePasswordRequest request = new ChangePasswordRequest(username,oldPassword,newPassword);

        MvcResult mvcResult = mockMvc.perform(
                        put(path)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andReturn();
        scenarioContext.setMvcResult(mvcResult);
    }

    @When("the client PATCHes to {string} with username {string}")
    public void patchChangeActive(String path, String username) throws Exception {
        MvcResult result = mockMvc.perform(
                patch(path)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(username)
        ).andReturn();

        scenarioContext.setMvcResult(result);
    }


    @Then("the password change is delegated for {string} with {string} and {string}")
    public void passwordChangeDelegated(String username, String oldPassword, String newPassword) {
        verify(userService).changePassword(eq(username), eq(oldPassword), eq(newPassword));
    }

    @Then("the active flag is toggled for user {string}")
    public void activeFlagToggled(String username) {
        verify(userService).changeActiveStatus(eq(username));
    }
}