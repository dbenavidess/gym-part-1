package com.dbenavidess.gym_part_1.infrastructure.logging.service;

import com.dbenavidess.gym_part_1.domain.model.Trainer;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
@Component
public class TrainerServiceLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(TrainerServiceLoggingAspect.class);


    // Pointcut that matches all methods in TrainerService
    @Pointcut("execution(* com.dbenavidess.gym_part_1.application.service.TrainerService.*(..))")
    public void trainerServiceMethods() {}

    // Logs before method execution
    @Before("trainerServiceMethods()")
    public void logMethodEntry(JoinPoint joinPoint) {

        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        logger.debug("Entering method: {} with arguments: {}", methodName, args);
    }

    // Logs method execution time and return values
    @Around("trainerServiceMethods()")
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

    // Specific advice for create trainer method
    @AfterReturning(
            pointcut = "execution(* com.dbenavidess.gym_part_1.application.service.TrainerService.createTrainer(..)) && args(trainer)",
            returning = "result"
    )
    public void logTrainerCreation(JoinPoint joinPoint, Trainer trainer, Trainer result) {
        logger.info("New trainer created successfully - ID: {}",
                result.getId());
    }

    // Specific advice for updating trainer
    @AfterReturning(
            pointcut = "execution(* com.dbenavidess.gym_part_1.application.service.TrainerService.updateTrainer(..)) && args(trainer)",
            returning = "result"
    )
    public void logTrainerUpdate(JoinPoint joinPoint, Trainer trainer, Trainer result) {
        logger.info("Trainer updated successfully - ID: {}", result.getId());
    }

    // Specific advice for deleting trainer
    @After("execution(* com.dbenavidess.gym_part_1.application.service.TrainerService.deleteTrainer(..)) && args(id)")
    public void logTrainerDeletion(JoinPoint joinPoint, UUID id) {
        logger.info("Trainer deleted successfully - ID: {}", id);
    }

    // Specific advice for retrieving trainer by username
    @AfterReturning(
            pointcut = "execution(* com.dbenavidess.gym_part_1.application.service.TrainerService.getTrainerByUsername(..)) && args(username)",
            returning = "result"
    )
    public void logTrainerRetrievalByUsername(JoinPoint joinPoint, String username, Trainer result) {
        if (result == null) {
            logger.warn("Trainer retrieval failed - Username not found: {}", username);
        } else {
            logger.info("Trainer retrieved successfully - Username: {}", username);
        }
    }

    // Handle exceptions
    @AfterThrowing(
            pointcut = "trainerServiceMethods()",
            throwing = "exception"
    )
    public void logException(JoinPoint joinPoint, Exception exception) {
        String methodName = joinPoint.getSignature().getName();
        logger.error("Exception occurred in TrainerService.{}: {} - {}",
                methodName, exception.getClass().getSimpleName(), exception.getMessage());
    }
}
