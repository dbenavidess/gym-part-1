package com.dbenavidess.gym_part_1.integrationTest.steps;

import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;

public class CommonStepDefinitions extends CucumberSpringConfiguration {

    @Autowired
    private ScenarioContext scenarioContext;

    private MvcResult currentResult() {
        MvcResult result = scenarioContext.getMvcResult();
        assertThat(result).as("No HTTP call registered in ScenarioContext").isNotNull();
        return result;
    }

    @Then("the response status is {int}")
    public void theResponseStatusIs(int expectedStatus) {
        assertThat(scenarioContext.getMvcResult())
                .as("MvcResult should be present – make sure a request step ran before this check")
                .isNotNull();

        assertThat(scenarioContext.getMvcResult().getResponse().getStatus())
                .isEqualTo(expectedStatus);
    }

    @Then("the response body contains {string}")
    public void theResponseBodyContains(String fragment) throws Exception {
        assertThat(currentResult().getResponse().getContentAsString()).contains(fragment);
    }

    @Then("the response JSON has {string} equal to {string}")
    public void theResponseJsonHasEqualTo(String jsonPath, String expectedValue) throws Exception {
        String body = currentResult().getResponse().getContentAsString();
        Object value = JsonPath.parse(body).read(jsonPath);
        assertThat(String.valueOf(value)).isEqualTo(expectedValue);
    }

}