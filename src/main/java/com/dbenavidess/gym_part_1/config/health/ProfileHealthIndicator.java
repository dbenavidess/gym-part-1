package com.dbenavidess.gym_part_1.config.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component("profile")
public class ProfileHealthIndicator implements HealthIndicator {

    private final Environment environment;

    public ProfileHealthIndicator(Environment environment) {
        this.environment = environment;
    }

    @Override
    public Health health() {
        String[] activeProfiles = environment.getActiveProfiles();
        String[] defaultProfiles = environment.getDefaultProfiles();

        Health.Builder builder = Health.up();

        if (activeProfiles.length > 0) {
            builder.withDetail("activeProfiles", activeProfiles)
                    .withDetail("profileCount", activeProfiles.length);
        } else {
            builder.withDetail("activeProfiles", defaultProfiles)
                    .withDetail("profileCount", defaultProfiles.length)
                    .withDetail("note", "Using default profiles");
        }

        return builder.build();
    }

}