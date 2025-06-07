package com.dbenavidess.gym_part_1.config.health;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpointDiscoverer;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.boot.actuate.endpoint.web.WebOperation;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;


@Component("endpoints")
public class AllEndpointsHealthIndicator implements HealthIndicator {

    private final WebEndpointDiscoverer webEndpointDiscoverer;
    private final RequestMappingHandlerMapping handlerMapping;

    public AllEndpointsHealthIndicator(
            WebEndpointDiscoverer webEndpointDiscoverer,
            @Qualifier("requestMappingHandlerMapping") RequestMappingHandlerMapping handlerMapping) {
        this.webEndpointDiscoverer = webEndpointDiscoverer;
        this.handlerMapping = handlerMapping;
    }

    @Override
    public Health health() {
        List<String> actuatorEndpoints = webEndpointDiscoverer.getEndpoints().stream()
                .flatMap(e -> e.getOperations().stream())
                .map(WebOperation::getRequestPredicate)
                .map(p -> p.getHttpMethod() + " " + p.getPath())
                .toList();

        List<String> restEndpoints = handlerMapping.getHandlerMethods().keySet()
                .stream()
                .filter(key -> !key.getMethodsCondition().isEmpty())
                .map(key -> key.getMethodsCondition().getMethods() + " " + key.getActivePatternsCondition())
                .toList();

        List<String> allEndpoints = new ArrayList<>();
        allEndpoints.addAll(actuatorEndpoints);
        allEndpoints.addAll(restEndpoints);

        return Health.up()
                .withDetail("endpoints", allEndpoints)
                .build();
    }
}