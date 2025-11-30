package com.dbenavidess.gym_part_1.config;

import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActiveMQConfigSupport {
    @Bean
    @ConfigurationProperties(prefix = "spring.activemq")
    public ActiveMQProperties activeMQProperties() {
        return new ActiveMQProperties();
    }
}
