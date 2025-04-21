package com.dbenavidess.gym_part_1;

import com.dbenavidess.gym_part_1.application.TrainerService;
import com.dbenavidess.gym_part_1.domain.model.Trainer;
import com.dbenavidess.gym_part_1.domain.model.User;
import com.dbenavidess.gym_part_1.domain.repository.TrainingTypeRepository;
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
        assertNotNull(createdTrainer);
        assertNotNull(createdTrainer.getId());
        assertEquals("Daniel" + "." + "Benavides",createdTrainer.getUser().getUsername());
        assertEquals("Daniel" + "." + "Benavides1",createdTrainer2.getUser().getUsername());

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
        Trainer trainer = new Trainer(trainingTypeRepository.getByName("resistance"), user);
        Trainer createdTrainer = trainerService.createTrainer(trainer);

        // Act

        Trainer updatedTrainer = new Trainer(trainer.getId(),user,trainingTypeRepository.getByName("yoga"));
        trainerService.updateTrainer(updatedTrainer);

        // Assert
        assertNotNull(updatedTrainer);
        assertNotEquals(createdTrainer, trainerService.getTrainer(updatedTrainer.getId()));
        assertEquals(trainerService.getTrainer(updatedTrainer.getId()).getSpecialization().getName(), "yoga");

        trainerService.deleteTrainer(createdTrainer.getId());
    }

    @Test
    public void testGetTrainer() {
        // Arrange
        User user = new User("Daniel","Benavides",true, userRepository);
        Trainer trainer = new Trainer(trainingTypeRepository.getByName("zumba"), user);
        Trainer createdTrainer = trainerService.createTrainer(trainer);

        // Act
        Trainer foundTrainer = trainerService.getTrainer(trainer.getId());

        // Assert
        assertNotNull(foundTrainer);
        assertEquals(createdTrainer.getId(), foundTrainer.getId());

        trainerService.deleteTrainer(createdTrainer.getId());

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
        assertEquals(2, result.size());

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
