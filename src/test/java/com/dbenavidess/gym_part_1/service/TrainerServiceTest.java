package com.dbenavidess.gym_part_1.service;

import com.dbenavidess.gym_part_1.domain.model.Trainer;
import com.dbenavidess.gym_part_1.domain.model.User;
import com.dbenavidess.gym_part_1.domain.repository.TrainingTypeRepository;
import com.dbenavidess.gym_part_1.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest()
public class TrainerServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private TrainingTypeRepository trainingTypeRepository;

    @Test
    public void testCreateTrainerSuccess() {
        // Arrange
        User user = new User("Daniel","Benavides",true, userRepository);
        Trainer trainer = new Trainer(trainingTypeRepository.getByName("zumba"), user);
        Trainer createdTrainer = trainerService.createTrainer(trainer);

        User user2 = new User("Daniel","Benavides",true, userRepository);
        Trainer trainer2 = new Trainer(trainingTypeRepository.getByName("fitness"), user2);
        Trainer createdTrainer2 = trainerService.createTrainer(trainer2);

        // Assert
        assertNotNull(createdTrainer.getId());
        assertTrue(createdTrainer.getUser().getUsername().matches("Daniel.Benavides[0-9]*"));
        assertTrue(createdTrainer2.getUser().getUsername().matches("Daniel.Benavides[0-9]+"));

        trainerService.deleteTrainer(createdTrainer.getId());
        trainerService.deleteTrainer(createdTrainer2.getId());
    }

    @Test
    public void testCreateTrainerFailure() {
        // Arrange
        Trainer trainer = new Trainer(trainingTypeRepository.getByName("fitness"), null);

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            trainerService.createTrainer(trainer);
        });
        assertEquals("Invalid Trainer", exception.getMessage());
    }

    @Test
    public void testUpdateTrainer() {
        // Arrange
        User user = new User("Daniel","Benavides",true, userRepository);
        Trainer createdTrainer = trainerService.createTrainer(
                new Trainer(trainingTypeRepository.getByName("resistance"), user)
        );

        // Act

        User updatedUser = new User(user.getUsername(),"CoolerDaniel","Benavides",true);

        Trainer updatedTrainer = trainerService.updateTrainer(
                new Trainer(createdTrainer.getId(),updatedUser,createdTrainer.getSpecialization())
        );


        // Assert
        assertNotNull(updatedTrainer);
        assertNotEquals(createdTrainer, trainerService.getTrainerByUsername(updatedTrainer.getUser().getUsername()));
        assertEquals(trainerService.getTrainerByUsername(updatedTrainer.getUser().getUsername()).getUser().getFirstName(), "CoolerDaniel");

        trainerService.deleteTrainer(createdTrainer.getId());
    }

    @Test
    public void testGetTrainer() {
        // Arrange
        User user = new User("Daniel","Benavides",true, userRepository);
        Trainer trainer = new Trainer(trainingTypeRepository.getByName("zumba"), user);
        Trainer createdTrainer = trainerService.createTrainer(trainer);

        // Act
        Trainer foundTrainer = trainerService.getTrainerByUsername(trainer.getUser().getUsername());

        // Assert
        assertNotNull(foundTrainer);
        assertEquals(createdTrainer.getId(), foundTrainer.getId());

        trainerService.deleteTrainer(createdTrainer.getId());

    }

    @Test
    public void testGetTrainerNotFound() {
        // Arrange
        String invalidUsername = "@InvalidUsername";

        // Act
        Trainer trainer = trainerService.getTrainerByUsername(invalidUsername);

        // Assert
        assertNull(trainer);
    }

    @Test
    public void testGetAllTrainers() {
        // Arrange
        User user = new User("Daniel","Benavides",true, userRepository);
        Trainer trainer = new Trainer(trainingTypeRepository.getByName("zumba"), user);
        Trainer createdTrainer = trainerService.createTrainer(trainer);

        User user2 = new User("Juan","Lopez",true, userRepository);
        Trainer trainer2 = new Trainer(trainingTypeRepository.getByName("zumba"), user2);
        Trainer createdTrainer2 = trainerService.createTrainer(trainer2);

        List<Trainer> trainers = List.of(trainer, trainer2);

        // Act
        List<Trainer> result = trainerService.getAllTrainers();

        // Assert
        assertNotNull(result);
        assertTrue( result.size() >= 2);

        trainerService.deleteTrainer(createdTrainer.getId());
        trainerService.deleteTrainer(createdTrainer2.getId());
    }

    @Test
    public void testGetTrainerByUsername(){

        // Arrange
        User user = new User("Daniel","Benavides",true, userRepository);
        Trainer trainer = new Trainer(trainingTypeRepository.getByName("zumba"), user);
        Trainer createdTrainer = trainerService.createTrainer(trainer);

        // Act
        Trainer foundTrainer = trainerService.getTrainerByUsername(trainer.getUser().getUsername());

        // Assert
        assertEquals(createdTrainer.getId(), foundTrainer.getId());

        trainerService.deleteTrainer(trainer.getId());

    }
}
