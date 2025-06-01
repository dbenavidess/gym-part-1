package com.dbenavidess.gym_part_1.service;

import com.dbenavidess.gym_part_1.application.service.TraineeService;
import com.dbenavidess.gym_part_1.application.service.TrainerService;
import com.dbenavidess.gym_part_1.domain.model.Trainee;
import com.dbenavidess.gym_part_1.domain.model.Trainer;
import com.dbenavidess.gym_part_1.domain.model.User;
import com.dbenavidess.gym_part_1.domain.repository.TrainingTypeRepository;
import com.dbenavidess.gym_part_1.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest()
public class TraineeServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TraineeService traineeService;

    @Autowired
    private TrainingTypeRepository typeRepository;

    @Autowired
    private TrainerService trainerService;

    @Test
    public void testCreateTraineeSuccess() {
        // Arrange
        User user = new User("Daniel","Benavides",true, userRepository);
        Trainee createdTrainee = traineeService.createTrainee(new Trainee("my house", Date.valueOf("1997-07-19"), user));

        User user2 = new User("Daniel","Benavides",true, userRepository);
        Trainee createdTrainee2 = traineeService.createTrainee(new Trainee("my house", Date.valueOf("1997-07-19"), user2));

        // Assert
        assertNotNull(createdTrainee);
        assertNotNull(createdTrainee2);
        assertTrue(createdTrainee2.getUser().getUsername().matches("Daniel.Benavides[0-9]*"));

        traineeService.deleteTrainee(createdTrainee.getId());
        traineeService.deleteTrainee(createdTrainee2.getId());
    }

    @Test
    public void testCreateTraineeFailure() {
        // Arrange
        Trainee trainee = new Trainee("address", Date.valueOf("1997-07-19"), null);

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            traineeService.createTrainee(trainee);
        });
        assertEquals("Invalid Trainee", exception.getMessage());
    }

    @Test
    public void testUpdateTrainee() {
        // Arrange
        User user = new User("Daniel","Benavides",true, userRepository);
        Trainee createdTrainee = traineeService.createTrainee(new Trainee("my house", Date.valueOf("1997-07-19"), user));

        // Act
        Trainee updatedTrainee = new Trainee(createdTrainee.getId(),"new address",createdTrainee.getDateOfBirth(),user);
        traineeService.updateTrainee(updatedTrainee);

        // Assert
        assertNotNull(updatedTrainee);
        assertNotEquals(createdTrainee, traineeService.getTraineeByUsername(updatedTrainee.getUser().getUsername()));
        assertEquals(traineeService.getTraineeByUsername(updatedTrainee.getUser().getUsername()).getAddress(), "new address" );

        traineeService.deleteTrainee(createdTrainee.getId());
    }

    @Test
    public void testGetTraineeFound() {
        // Arrange
        User user = new User("Daniel","Benavides",true, userRepository);
        Trainee trainee = new Trainee("my house", Date.valueOf("1997-07-19"), user);
        Trainee createdTrainee = traineeService.createTrainee(trainee);

        // Act
        Trainee foundTrainee = traineeService.getTraineeByUsername(trainee.getUser().getUsername());

        // Assert
        assertNotNull(foundTrainee);
        assertEquals(createdTrainee.getId(), foundTrainee.getId());

        traineeService.deleteTrainee(createdTrainee.getId());
    }

    @Test
    public void testGetTraineeNotFound() {
        // Arrange
        String invalidUsername = "@InvalidUsername";

        // Act
        Trainee trainee = traineeService.getTraineeByUsername(invalidUsername);

        // Assert
        assertNull(trainee);
    }

    @Test
    public void testDeleteTrainee(){
        // Arrange
        User user = new User("Daniel","Benavides",true, userRepository);
        Trainee trainee = new Trainee("my house", Date.valueOf("1997-07-19"), user);
        Trainee createdTrainee = traineeService.createTrainee(trainee);

        // Act
        traineeService.deleteTrainee(trainee.getId());

        // Assert
        assertNull(traineeService.getTraineeByUsername(createdTrainee.getUser().getUsername()));

    }

    @Test
    public void testGetTraineeByUsername(){

        // Arrange
        User user = new User("Daniel","Benavides",true, userRepository);
        Trainee trainee = new Trainee("my house", Date.valueOf("1997-07-19"), user);
        Trainee createdTrainee = traineeService.createTrainee(trainee);

        // Act
        Trainee foundTrainee = traineeService.getTraineeByUsername(trainee.getUser().getUsername());

        // Assert
        assertEquals(createdTrainee.getId(), foundTrainee.getId());

        traineeService.deleteTrainee(createdTrainee.getId());

    }

    @Test
    public void testGetNotAssignedTrainerList(){
        // Arrange
        User user = new User("Daniel","Benavides",true, userRepository);
        Trainee createdTrainee = traineeService.createTrainee(new Trainee("my house", Date.valueOf("1997-07-19"), user));

        User user2 = new User("John","Wick",true, userRepository);
        Trainer createdTrainer = trainerService.createTrainer(new Trainer(typeRepository.getByName("resistance"), user2));

        User user3 = new User("Tony","Hawk",true, userRepository);
        Trainer createdTrainer2 = trainerService.createTrainer(new Trainer(typeRepository.getByName("fitness"), user3));

        List<Trainer> trainers = traineeService.updateTraineeTrainerList(
                List.of(createdTrainer.getUser().getUsername(),createdTrainer2.getUser().getUsername()),
                createdTrainee.getUser().getUsername());


        // Act
        Trainee foundTrainee = traineeService.getTraineeByUsername(createdTrainee.getUser().getUsername());

        // Assert
        assertNotEquals(traineeService.getNotAssignedTrainerList(foundTrainee),trainers);
        assertNotEquals(traineeService.getNotAssignedTrainerList(foundTrainee),null);
        assertFalse(trainers.contains(createdTrainer2));

        traineeService.deleteTrainee(createdTrainee.getId());
        trainerService.deleteTrainer(createdTrainer.getId());
        trainerService.deleteTrainer(createdTrainer2.getId());

    }

    @Test
    public void testDeleteByUsername(){
        // Arrange
        User user = new User("Daniel","Benavides",true, userRepository);
        Trainee trainee = new Trainee("my house", Date.valueOf("1997-07-19"), user);
        Trainee createdTrainee = traineeService.createTrainee(trainee);

        // Act
        traineeService.deleteByUsername(user.getUsername());
        Trainee foundTrainee = traineeService.getTraineeByUsername(createdTrainee.getUser().getUsername());

        // Assert
        assertNull(foundTrainee);

    }
}
