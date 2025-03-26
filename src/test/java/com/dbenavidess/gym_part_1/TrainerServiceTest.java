package com.dbenavidess.gym_part_1;

import com.dbenavidess.gym_part_1.application.TrainerService;
import com.dbenavidess.gym_part_1.domain.model.Trainer;
import com.dbenavidess.gym_part_1.domain.model.TrainingType;
import com.dbenavidess.gym_part_1.domain.model.User;
import com.dbenavidess.gym_part_1.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest()
public class TrainerServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrainerService trainerService;

    @Test
    public void testCreateTrainerSuccess() {
        // Arrange
        User user = new User("Daniel","Benavides",true, userRepository);
        Trainer trainer = new Trainer(TrainingType.valueOf("zumba"), user);

        Trainer createdTrainer = trainerService.createTrainer(trainer);

        User user2 = new User("Daniel","Benavides",true, userRepository);
        Trainer trainer2 = new Trainer(TrainingType.valueOf("zumba"), user2);

        Trainer createdTrainer2 = trainerService.createTrainer(trainer2);

        // Assert
        assertNotNull(createdTrainer);
        assertNotNull(createdTrainer.getId());
        assertEquals("Daniel" + "." + "Benavides",createdTrainer.getUser().getUsername());
        assertEquals("Daniel" + "." + "Benavides1",trainer2.getUser().getUsername());
    }

    @Test
    public void testCreateTrainerFailure() {
        // Arrange
        Trainer trainer = new Trainer(TrainingType.valueOf("zumba"), null);

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
        Trainer trainer = new Trainer(TrainingType.valueOf("zumba"), user);

        Trainer createdTrainer = trainerService.createTrainer(trainer);

        // Act

        Trainer updatedTrainer = new Trainer(trainer.getId(),user,TrainingType.valueOf("yoga"));
        trainerService.updateTrainer(updatedTrainer);

        // Assert
        assertNotNull(updatedTrainer);
        assertNotEquals(trainer, trainerService.getTrainer(updatedTrainer.getId()));
        assertEquals(trainerService.getTrainer(updatedTrainer.getId()).getSpecialization(), TrainingType.valueOf("yoga"));
    }

    @Test
    public void testGetTrainerFound() {
        // Arrange
        User user = new User("Daniel","Benavides",true, userRepository);
        Trainer trainer = new Trainer(TrainingType.valueOf("zumba"), user);

        Trainer createdTrainer = trainerService.createTrainer(trainer);

        // Act
        Trainer foundTrainer = trainerService.getTrainer(trainer.getId());

        // Assert
        assertNotNull(foundTrainer);
        assertEquals(trainer.getId(), foundTrainer.getId());
    }

    @Test
    public void testGetTrainerNotFound() {
        // Arrange
        UUID trainerId = UUID.randomUUID();

        // Act
        Trainer trainer = trainerService.getTrainer(trainerId);

        // Assert
        assertNull(trainer);
    }

    @Test
    public void testGetAllTrainers() {
        // Arrange
        User user = new User("Daniel","Benavides",true, userRepository);
        Trainer trainer = new Trainer(TrainingType.valueOf("zumba"), user);

        Trainer createdTrainer = trainerService.createTrainer(trainer);

        User user2 = new User("Juan","Lopez",true, userRepository);
        Trainer trainer2 = new Trainer(TrainingType.valueOf("fitness"), user2);

        Trainer createdTrainer2 = trainerService.createTrainer(trainer2);
        List<Trainer> trainers = List.of(trainer, trainer2);

        // Act
        List<Trainer> result = trainerService.getAllTrainers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }
}
