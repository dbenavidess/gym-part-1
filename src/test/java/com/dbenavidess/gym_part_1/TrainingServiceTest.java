package com.dbenavidess.gym_part_1;

import com.dbenavidess.gym_part_1.application.TraineeService;
import com.dbenavidess.gym_part_1.application.TrainerService;
import com.dbenavidess.gym_part_1.application.TrainingService;
import com.dbenavidess.gym_part_1.domain.model.*;
import com.dbenavidess.gym_part_1.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest()
public class TrainingServiceTest {

    @Autowired
    private UserRepository userRepository;

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
        Trainer trainer = new Trainer(TrainingType.valueOf("zumba"), user);

        Trainer createdTrainer = trainerService.createTrainer(trainer);

        User user2 = new User("Juan","Lopez",true, userRepository);
        Trainee trainee = new Trainee("Juan's address", LocalDate.of(1990,8,10), user2);

        Trainee createdTrainee = traineeService.createTrainee(trainee);

        Training training = new Training(trainer,trainee,"zumba class",TrainingType.zumba,LocalDate.now(),60);
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
        Training training = new Training(null,null, "", TrainingType.yoga, LocalDate.now(), 10);


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
        Trainer trainer = new Trainer(TrainingType.valueOf("zumba"), user);

        Trainer createdTrainer = trainerService.createTrainer(trainer);

        User user2 = new User("Juan","Lopez",true, userRepository);
        Trainee trainee = new Trainee("Juan's address", LocalDate.of(1990,8,10), user2);

        Trainee createdTrainee = traineeService.createTrainee(trainee);

        Training training = new Training(trainer,trainee,"zumba class",TrainingType.zumba,LocalDate.now(),60);
        Training createdTraining = trainingService.createTraining(training);

        // Act
        Training foundTraining = trainingService.getTraining(training.getId());

        // Assert
        assertNotNull(foundTraining);
        assertEquals(training.getId(), foundTraining.getId());
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

}
