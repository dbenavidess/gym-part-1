package com.dbenavidess.gym_part_1.integrationTest;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.*;


@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME,
        value = "pretty, summary")
@ConfigurationParameter(key = ANSI_COLORS_DISABLED_PROPERTY_NAME, value = "false")
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(
        key = GLUE_PROPERTY_NAME,
        value = "com.dbenavidess.gym_part_1.integrationTest.steps"
)
public class CucumberIntegrationTest {
}