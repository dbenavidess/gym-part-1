package com.dbenavidess.gym_part_1.infrastructure.logging.service;

import com.dbenavidess.gym_part_1.domain.model.Trainee;
import com.dbenavidess.gym_part_1.domain.model.Trainer;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Aspect
@Component
public class TraineeServiceLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(TraineeServiceLoggingAspect.class);

    // Pointcut that matches all methods in TraineeService
    @Pointcut("execution(* com.dbenavidess.gym_part_1.application.service.TraineeService.*(..))")
    public void traineeServiceMethods() {}

    // Logs before method execution
    @Before("traineeServiceMethods()")
    public void logMethodEntry(JoinPoint joinPoint) {
        String transactionId = MDC.get("transactionId");
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        logger.debug("Entering method: {} with arguments: {}", methodName, args);
    }

    // Logs method execution time and return values
    @Around("traineeServiceMethods()")
    public Object logMethodExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().getName();

        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;

            logger.debug("Method {} executed in {} ms", methodName, executionTime);

            return result;
        } catch (Exception e) {
            logger.error("Exception in method {}: {}", methodName, e.getMessage());
            throw e;
        }
    }

    // Specific advice for create trainee method
    @AfterReturning(
            pointcut = "execution(* com.dbenavidess.gym_part_1.application.service.TraineeService.createTrainee(..)) && args(trainee)",
            returning = "result"
    )
    public void logTraineeCreation(JoinPoint joinPoint, Trainee trainee, Trainee result) {
        logger.info("New trainee created successfully - ID: {}", result.getId());
    }

    // Specific advice for updating trainee
    @AfterReturning(
            pointcut = "execution(* com.dbenavidess.gym_part_1.application.service.TraineeService.updateTrainee(..)) && args(trainee)",
            returning = "result"
    )
    public void logTraineeUpdate(JoinPoint joinPoint, Trainee result) {
        logger.info("Trainee updated successfully - ID: {}", result.getId());
    }

    // Specific advice for deleting trainee
    @After("execution(* com.dbenavidess.gym_part_1.application.service.TraineeService.deleteTrainee(..)) && args(id)")
    public void logTraineeDeletion(JoinPoint joinPoint, UUID id) {
        logger.info("Trainee deleted successfully - ID: {}", id);
    }

    // Specific advice for retrieving trainee by username
    @AfterReturning(
            pointcut = "execution(* com.dbenavidess.gym_part_1.application.service.TraineeService.getTraineeByUsername(..)) && args(username)",
            returning = "result"
    )
    public void logTraineeRetrievalByUsername(JoinPoint joinPoint, String username, Trainee result) {
        if (result == null) {
            logger.warn("Trainee retrieval failed - Username not found: {}", username);
        } else {
            logger.info("Trainee retrieved successfully - Username: {}", username);
        }
    }

    // Advice for updating trainee's trainer list
    @AfterReturning(
            pointcut = "execution(* com.dbenavidess.gym_part_1.application.service.TraineeService.updateTraineeTrainerList(..)) && args(trainers, username)",
            returning = "result"
    )
    public void logUpdateTraineeTrainerList(JoinPoint joinPoint, List<String> trainers, String username, List<Trainer> result) {
        logger.info("Updated trainer list for trainee with username: {}, assigned trainers count: {}",
                username, result != null ? result.size() : 0);
    }

    // Handle exceptions
    @AfterThrowing(
            pointcut = "traineeServiceMethods()",
            throwing = "exception"
    )
    public void logException(JoinPoint joinPoint, Exception exception) {
        String methodName = joinPoint.getSignature().getName();
        logger.error("Exception occurred in TraineeService.{}: {} - {}",
                methodName, exception.getClass().getSimpleName(), exception.getMessage());
    }
}