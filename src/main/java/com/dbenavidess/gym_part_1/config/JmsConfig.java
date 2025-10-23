package com.dbenavidess.gym_part_1.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

@Configuration
@EnableJms
public class JmsConfig {

    @Bean
    public SingleConnectionFactory singleConnectionFactory() {
        // ActiveMQ native connection factory
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL("tcp://localhost:61616");
        activeMQConnectionFactory.setUserName("admin");
        activeMQConnectionFactory.setPassword("admin");

        // Wrap with Spring's SingleConnectionFactory
        return new SingleConnectionFactory(activeMQConnectionFactory);
    }

    @Bean
    public JmsTemplate jmsTemplate(SingleConnectionFactory connectionFactory) {
        JmsTemplate template = new JmsTemplate(connectionFactory);
        template.setPubSubDomain(false); // false = queue mode, true = topic mode
        return template;
    }
}