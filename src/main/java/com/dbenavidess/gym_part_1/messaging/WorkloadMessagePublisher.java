package com.dbenavidess.gym_part_1.messaging;


import com.dbenavidess.gym_part_1.infrastructure.request.workload.WorkloadRequest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class WorkloadMessagePublisher {

    private final JmsTemplate jmsTemplate;
    private static final String WORKLOAD_QUEUE = "trainer.workload.queue";

    public WorkloadMessagePublisher(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendWorkloadEvent(WorkloadRequest request) {
        jmsTemplate.convertAndSend(WORKLOAD_QUEUE, request, message -> {
            message.setStringProperty("_type", "WorkloadRequest");
            return message;
        });
        System.out.println("Sent JMS workload event for trainer: " + request.getTrainerUsername());
    }
}