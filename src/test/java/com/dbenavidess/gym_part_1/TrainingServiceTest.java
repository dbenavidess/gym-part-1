package com.dbenavidess.gym_part_1;

import com.dbenavidess.gym_part_1.application.TraineeService;
import com.dbenavidess.gym_part_1.application.TrainerService;
import com.dbenavidess.gym_part_1.application.TrainingService;
import com.dbenavidess.gym_part_1.domain.model.*;
import com.dbenavidess.gym_part_1.domain.repository.TrainingTypeRepository;
import com.dbenavidess.gym_part_1.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;
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

    @Test
    public void testCreateTrainingSuccess() {
        // Arrange
        User user = new User("Daniel","Benavides",true, userRepository);
        Trainer trainer = new Trainer(trainingTypeRepository.getByName("zumba"), user);

        Trainer createdTrainer = trainerService.createTrainer(trainer);

        User user2 = new User("Juan","Lopez",true, userRepository);
        Trainee trainee = new Trainee("Juan's address", Date.valueOf("1990-08-10"), user2);

        Trainee createdTrainee = traineeService.createTrainee(trainee);

        Training training = new Training(trainer,
                trainee,
                "zumba class",
                trainingTypeRepository.getByName("zumba"),
                Date.valueOf("2025-04-21"),
                60);
        Training createdTraining = trainingService.createTraining(training);

        // Assert
        assertNotNull(createdTraining);
        assertNotNull(createdTraining.getId());
        assertEquals(trainee,training.getTrainee());
        assertEquals(trainer,training.getTrainer());
    }

    @Test
    public void testCreateTrainingFailure() {
        // Arrange
        Training training = new Training(null,null, "", null, Date.valueOf("1990-08-10"), 10);


        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            trainingService.createTraining(training);
        });
        assertEquals("Invalid Training", exception.getMessage());
    }

    @Test
    public void testGetTrainingFound() {
        // Arrange
        User user = new User("Daniel","Benavides",true, userRepository);
        Trainer trainer = new Trainer(trainingTypeRepository.getByName("zumba"), user);
        Trainer createdTrainer = trainerService.createTrainer(trainer);

        User user2 = new User("Juan","Lopez",true, userRepository);
        Trainee trainee = new Trainee("Juan's address", Date.valueOf("1990-08-10"), user2);
        Trainee createdTrainee = traineeService.createTrainee(trainee);

        Training training = new Training(
                trainer,
                trainee,
                "zumba class",
                trainingTypeRepository.getByName("zumba"),
                Date.valueOf("2025-04-21"),
                60);

        Training createdTraining = trainingService.createTraining(training);

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
        User user = new User("Daniel","Benavides",true, userRepository);
        Trainer trainer = new Trainer(trainingTypeRepository.getByName("zumba"), user);
        Trainer createdTrainer = trainerService.createTrainer(trainer);

        User user2 = new User("Juan","Lopez",true, userRepository);
        Trainee trainee = new Trainee("Juan's address", Date.valueOf("1990-08-10"), user2);
        Trainee createdTrainee = traineeService.createTrainee(trainee);

        Training training = new Training(trainer,
                trainee,
                "zumba class",
                trainingTypeRepository.getByName("zumba"),
                Date.valueOf("2025-04-21"),
                60);
        Training createdTraining = trainingService.createTraining(training);

        Training training2 = new Training(trainer,
                trainee,
                "zumba class",
                trainingTypeRepository.getByName("zumba"),
                Date.valueOf("2025-04-19"),
                60);
        Training createdTraining2 = trainingService.createTraining(training2);

        //Act
        List<Training> result = trainingService.searchTrainings(
                createdTrainer.getUser().getUsername(),
                Date.valueOf("2025-04-21"),
                Date.valueOf("2025-04-30"),
                createdTrainee.getUser().getUsername(),
                "zumba");


        //Assert
        assertEquals(result.size(),1);

    }

}
