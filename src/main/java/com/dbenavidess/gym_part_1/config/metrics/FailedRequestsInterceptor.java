package com.dbenavidess.gym_part_1.config.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class FailedRequestsInterceptor implements HandlerInterceptor {

    private Counter failedRequestsCounter;

    public FailedRequestsInterceptor(MeterRegistry meterRegistry) {
        failedRequestsCounter = Counter.builder( "failed.requests")
                .description("Failed HTTP requests")
                .register(meterRegistry);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (response.getStatus() >= 400){
            failedRequestsCounter.increment();
        }
    }
}
