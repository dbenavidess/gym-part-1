package com.dbenavidess.gym_part_1.config.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class UserRegisterInterceptor implements HandlerInterceptor {

    private final Counter activeUserCounter;

    public UserRegisterInterceptor(MeterRegistry meterRegistry) {
        activeUserCounter = Counter.builder( "ActiveUsers")
                .description("Registered users")
                .register(meterRegistry);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        if(!request.getRequestURI().equals("/trainee")
                && !request.getRequestURI().equals("/trainee")){
            return;
        }

        if (!request.getMethod().equals("POST")){
            return;
        }

        if (response.getStatus() == 201){
            activeUserCounter.increment();
        }
    }

}
