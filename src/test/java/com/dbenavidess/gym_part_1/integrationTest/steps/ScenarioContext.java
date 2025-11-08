package com.dbenavidess.gym_part_1.integrationTest.steps;

import io.cucumber.spring.ScenarioScope;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MvcResult;

@Component
@ScenarioScope
public class ScenarioContext {

    private MvcResult mvcResult;

    public MvcResult getMvcResult() {
        return mvcResult;
    }

    public void setMvcResult(MvcResult mvcResult) {
        this.mvcResult = mvcResult;
    }
}