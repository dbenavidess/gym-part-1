package com.dbenavidess.gym_part_1;

import com.dbenavidess.gym_part_1.application.TrainerService;
import com.dbenavidess.gym_part_1.config.ProjectConfig;
import com.dbenavidess.gym_part_1.domain.model.Trainer;
import com.dbenavidess.gym_part_1.domain.model.TrainingType;
import com.dbenavidess.gym_part_1.domain.model.User;
import com.dbenavidess.gym_part_1.domain.repository.TrainerRepository;
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
    private TrainerRepository trainerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrainerService trainerService;

    @Test
    public void testCreateTrainerSuccess() {
        // Arrange
        User user = new User("Daniel","Benavides",true, userRepository);
        Trainer trainer = new Trainer(TrainingType.valueOf("zumba"), user);

        // Act
        Trainer createdTrainer = trainerService.createTrainer(trainer);

        // Assert
        assertNotNull(createdTrainer);
        assertEquals(trainer.getId(), createdTrainer.getId());
    }

    @Test
    public void testCreateTrainerFailure() {
        // Arrange
        Trainer trainer = new Trainer();

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            trainerService.createTrainer(trainer);
        });
        assertEquals("Invalid Trainer", exception.getMessage());
    }

    @Test
    public void testUpdateTrainer() {
        // Arrange
        Trainer trainer = new Trainer();

        // Act
        Trainer updatedTrainer = trainerService.updateTrainer(trainer);

        // Assert
        assertNotNull(updatedTrainer);
        assertEquals(trainer.getId(), updatedTrainer.getId());
    }

    @Test
    public void testGetTrainerFound() {
        // Arrange
        UUID trainerId = UUID.randomUUID();
        Trainer trainer = new Trainer();

        // Act
        Trainer foundTrainer = trainerService.getTrainer(trainerId);

        // Assert
        assertNotNull(foundTrainer);
        assertEquals(trainerId, foundTrainer.getId());
    }

    @Test
    public void testGetTrainerNotFound() {
        // Arrange
        //todo
        UUID trainerId = UUID.randomUUID();

        // Act
        Trainer trainer = trainerService.getTrainer(trainerId);

        // Assert
        assertNull(trainer);
    }

    @Test
    public void testGetAllTrainers() {
        // Arrange
        Trainer trainer1 = new Trainer();
        Trainer trainer2 = new Trainer();
        //todo initialize for every test
        List<Trainer> trainers = List.of(trainer1, trainer2);

        // Act
        List<Trainer> result = trainerService.getAllTrainers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }
}
