package com.dbenavidess.gym_part_1;

import com.dbenavidess.gym_part_1.application.TraineeService;
import com.dbenavidess.gym_part_1.application.TrainerService;
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
import java.util.UUID;

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
        Trainee trainee = new Trainee("my house", Date.valueOf("1997-07-19"), user);
        Trainee createdTrainee = traineeService.createTrainee(trainee);

        User user2 = new User("Daniel","Benavides",true, userRepository);
        Trainee trainee2 = new Trainee("my house", Date.valueOf("1997-07-19"), user2);
        Trainee createdTrainee2 = traineeService.createTrainee(trainee2);

        // Assert
        assertNotNull(createdTrainee);
        assertNotNull(createdTrainee.getId());
        assertEquals("Daniel" + "." + "Benavides",createdTrainee.getUser().getUsername());
        assertEquals("Daniel" + "." + "Benavides1",createdTrainee2.getUser().getUsername());

        traineeService.deleteTrainee(trainee.getId());
        traineeService.deleteTrainee(trainee2.getId());
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
        Trainee trainee = new Trainee("my house", Date.valueOf("1997-07-19"), user);
        Trainee createdTrainee = traineeService.createTrainee(trainee);

        // Act

        Trainee updatedTrainee = new Trainee(trainee.getId(),"new address",trainee.getDateOfBirth(),user);
        traineeService.updateTrainee(updatedTrainee);

        // Assert
        assertNotNull(updatedTrainee);
        assertNotEquals(createdTrainee, traineeService.getTrainee(updatedTrainee.getId()));
        assertEquals(traineeService.getTrainee(updatedTrainee.getId()).getAddress(), "new address" );

        traineeService.deleteTrainee(createdTrainee.getId());
    }

    @Test
    public void testGetTraineeFound() {
        // Arrange
        User user = new User("Daniel","Benavides",true, userRepository);
        Trainee trainee = new Trainee("my house", Date.valueOf("1997-07-19"), user);
        Trainee createdTrainee = traineeService.createTrainee(trainee);

        // Act
        Trainee foundTrainee = traineeService.getTrainee(trainee.getId());

        // Assert
        assertNotNull(foundTrainee);
        assertEquals(createdTrainee.getId(), foundTrainee.getId());

        traineeService.deleteTrainee(createdTrainee.getId());
    }

    @Test
    public void testGetTraineeNotFound() {
        // Arrange
        UUID traineeId = UUID.randomUUID();

        // Act
        Trainee trainee = traineeService.getTrainee(traineeId);

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
        assertNull(traineeService.getTrainee(createdTrainee.getId()));

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
        Trainee trainee = new Trainee("my house", Date.valueOf("1997-07-19"), user);
        Trainee createdTrainee = traineeService.createTrainee(trainee);

        User user2 = new User("John","Wick",true, userRepository);
        Trainer trainer = new Trainer(typeRepository.getByName("resistance"), user2);
        Trainer createdTrainer = trainerService.createTrainer(trainer);

        User user3 = new User("Tony","Hawk",true, userRepository);
        Trainer trainer2 = new Trainer(typeRepository.getByName("fitness"), user3);
        Trainer createdTrainer2 = trainerService.createTrainer(trainer2);

        List<Trainer> trainers = traineeService.addTrainerToTrainee(createdTrainee,createdTrainer);


        // Act
        Trainee foundTrainee = traineeService.getTraineeByUsername(createdTrainee.getUser().getUsername());

        // Assert
        assertNotEquals(traineeService.getNotAssignedTrainerList(foundTrainee),trainers);
        assertNotEquals(traineeService.getNotAssignedTrainerList(foundTrainee),null);
        assertFalse(trainers.contains(createdTrainer2));

        traineeService.deleteTrainee(trainee.getId());
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
