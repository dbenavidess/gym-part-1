package com.dbenavidess.gym_part_1.infrastructure.logging.service;

import com.dbenavidess.gym_part_1.domain.model.Training;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Aspect
@Component
public class TrainingServiceLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(TrainingServiceLoggingAspect.class);

    // Pointcut that matches all methods in TrainingService
    @Pointcut("execution(* com.dbenavidess.gym_part_1.application.service.TrainingService.*(..))")
    public void trainingServiceMethods() {}

    // Logs before method execution
    @Before("trainingServiceMethods()")
    public void logMethodEntry(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        logger.debug("Entering method: {} with arguments: {}", methodName, args);
    }

    // Logs method execution time and return values
    @Around("trainingServiceMethods()")
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

    // Specific advice for create training method
    @AfterReturning(
            pointcut = "execution(* com.dbenavidess.gym_part_1.application.service.TrainingService.createTraining(..)) && args(name, date, duration, trainingTypeName, trainerName, traineeName)",
            returning = "result"
    )
    public void logTrainingCreation(JoinPoint joinPoint, String name, Date date, Integer duration,
                                    String trainingTypeName, String trainerName, String traineeName, Training result) {
        logger.info("New training created successfully - ID: {}, Name: {}, Trainer: {}, Trainee: {}, Type: {}, Duration: {} minutes",
                result.getId(), name, trainerName, traineeName, trainingTypeName, duration);
    }

    // Specific advice for deleting training
    @After("execution(* com.dbenavidess.gym_part_1.application.service.TrainingService.deleteTraining(..)) && args(id)")
    public void logTrainingDeletion(JoinPoint joinPoint, UUID id) {
        logger.info("Training deleted successfully - ID: {}", id);
    }

    // Specific advice for retrieving training by ID
    @AfterReturning(
            pointcut = "execution(* com.dbenavidess.gym_part_1.application.service.TrainingService.getTraining(..)) && args(id)",
            returning = "result"
    )
    public void logTrainingRetrieval(JoinPoint joinPoint, UUID id, Training result) {
        if (result == null) {
            logger.warn("Training retrieval failed - ID not found: {}", id);
        } else {
            logger.info("Training retrieved successfully - ID: {}, Name: {}", id, result.getName());
        }
    }

    // Specific advice for searching trainings
    @AfterReturning(
            pointcut = "execution(* com.dbenavidess.gym_part_1.application.service.TrainingService.searchTrainings(..)) && args(trainerUsername, from, to, traineeUsername, trainingTypeName)",
            returning = "result"
    )
    public void logTrainingSearch(JoinPoint joinPoint, String trainerUsername, Date from, Date to,
                                  String traineeUsername, String trainingTypeName, List<Training> result) {
        StringBuilder searchCriteria = new StringBuilder();
        searchCriteria.append("Search criteria - ");

        if (trainerUsername != null && !trainerUsername.isEmpty()) {
            searchCriteria.append("Trainer: ").append(trainerUsername).append(", ");
        }
        if (traineeUsername != null && !traineeUsername.isEmpty()) {
            searchCriteria.append("Trainee: ").append(traineeUsername).append(", ");
        }
        if (trainingTypeName != null && !trainingTypeName.isEmpty()) {
            searchCriteria.append("Type: ").append(trainingTypeName).append(", ");
        }
        if (from != null) {
            searchCriteria.append("From: ").append(from).append(", ");
        }
        if (to != null) {
            searchCriteria.append("To: ").append(to).append(", ");
        }

        // Remove trailing comma and space
        String criteria = searchCriteria.toString();
        if (criteria.endsWith(", ")) {
            criteria = criteria.substring(0, criteria.length() - 2);
        }

        if (result == null || result.isEmpty()) {
            logger.info("Training search completed - No trainings found. {}", criteria);
        } else {
            logger.info("Training search completed - Found {} trainings. {}", result.size(), criteria);
        }
    }

    // Handle exceptions
    @AfterThrowing(
            pointcut = "trainingServiceMethods()",
            throwing = "exception"
    )
    public void logException(JoinPoint joinPoint, Exception exception) {
        String methodName = joinPoint.getSignature().getName();
        logger.error("Exception occurred in TrainingService.{}: {} - {}",
                methodName, exception.getClass().getSimpleName(), exception.getMessage());
    }
}