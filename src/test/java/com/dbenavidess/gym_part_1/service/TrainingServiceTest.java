package com.dbenavidess.gym_part_1.service;

import com.dbenavidess.gym_part_1.domain.model.*;
import com.dbenavidess.gym_part_1.domain.repository.TrainingTypeRepository;
import com.dbenavidess.gym_part_1.domain.repository.UserRepository;
import com.dbenavidess.gym_part_1.domain.util.PasswordEncryptionProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest()
public class TrainingServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrainingTypeRepository trainingTypeRepository;

    @Autowired
    private TrainingService trainingService;

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private TraineeService traineeService;

    @Autowired
    private PasswordEncryptionProvider passwordEncryptionProvider;

    @Test
    public void testCreateTrainingSuccess() {
        // Arrange
        User user = new User("Daniel","Benavides",true, userRepository, passwordEncryptionProvider);
        Trainer createdTrainer = trainerService.createTrainer(new Trainer(trainingTypeRepository.getByName("zumba"), user));

        User user2 = new User("Juan","Lopez",true, userRepository, passwordEncryptionProvider);
        Trainee createdTrainee = traineeService.createTrainee(new Trainee("Juan's address", Date.valueOf("1990-08-10"), user2));

        Training training = new Training(createdTrainer,
                createdTrainee,
                "zumba class",
                trainingTypeRepository.getByName("zumba"),
                Date.valueOf("2025-04-21"),
                60);

        Training createdTraining = trainingService.createTraining(
                "zumba class",
                Date.valueOf("2025-04-21"),
                60,
                "zumba",
                createdTrainer.getUser().getUsername(),
                createdTrainee.getUser().getUsername()
        );

        // Assert
        assertNotNull(createdTraining);
        assertNotNull(createdTraining.getId());
        assertEquals(createdTrainee,training.getTrainee());
        assertEquals(createdTrainer,training.getTrainer());
    }

    @Test
    public void testCreateTrainingFailure() {
        // Arrange

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            trainingService.createTraining("",
                    Date.valueOf("1990-08-10"),
                    10,
                    null,
                    "",
                    "");
        });
        assertEquals("Invalid Training", exception.getMessage());
    }

    @Test
    public void testGetTrainingFound() {
        // Arrange
        User user = new User("Daniel","Benavides",true, userRepository, passwordEncryptionProvider);
        Trainer createdTrainer = trainerService.createTrainer(new Trainer(trainingTypeRepository.getByName("zumba"), user));

        User user2 = new User("Juan","Lopez",true, userRepository, passwordEncryptionProvider);
        Trainee createdTrainee = traineeService.createTrainee(new Trainee("Juan's address", Date.valueOf("1990-08-10"), user2));

        Training createdTraining = trainingService.createTraining(
                "zumba class",
                Date.valueOf("2025-04-21"),
                60,
                "zumba",
                createdTrainer.getUser().getUsername(),
                createdTrainee.getUser().getUsername()
                );

        // Act
        Training foundTraining = trainingService.getTraining(createdTraining.getId());

        // Assert
        assertNotNull(foundTraining);
        assertEquals(createdTraining.getId(), foundTraining.getId());
    }

    @Test
    public void testGetTrainingNotFound() {
        // Arrange
        UUID trainingId = UUID.randomUUID();

        // Act
        Training training = trainingService.getTraining(trainingId);

        // Assert
        assertNull(training);
    }

    @Test
    public void testSearchTrainings(){

        // Arrange
        User user = new User("Daniel","Benavides",true, userRepository, passwordEncryptionProvider);
        Trainer createdTrainer = trainerService.createTrainer(new Trainer(trainingTypeRepository.getByName("zumba"), user));

        User user2 = new User("Juan","Lopez",true, userRepository, passwordEncryptionProvider);
        Trainee createdTrainee = traineeService.createTrainee(new Trainee("Juan's address", Date.valueOf("1990-08-10"), user2));

        Training createdTraining = trainingService.createTraining(
                "zumba class",
                Date.valueOf("2025-04-21"),
                60,
                "zumba",
                createdTrainer.getUser().getUsername(),
                createdTrainee.getUser().getUsername()
        );


        Training createdTraining2 = trainingService.createTraining(
                "zumba class",
                Date.valueOf("2025-04-19"),
                60,
                "zumba",
                createdTrainer.getUser().getUsername(),
                createdTrainee.getUser().getUsername()
        );
        //Act
        List<Training> result = trainingService.searchTrainings(
                createdTrainer.getUser().getUsername(),
                Date.valueOf("2025-04-21"),
                Date.valueOf("2025-04-30"),
                createdTrainee.getUser().getUsername(),
                "zumba");

        //Assert
        assertEquals(result.size(),1);

        trainerService.deleteTrainer(createdTrainer.getId());
        traineeService.deleteTrainee(createdTrainee.getId());
        trainingService.deleteTraining(createdTraining.getId());
        trainingService.deleteTraining(createdTraining2.getId());

    }

}
