package com.dbenavidess.gym_part_1;

import com.dbenavidess.gym_part_1.application.TraineeService;
import com.dbenavidess.gym_part_1.domain.model.Trainee;
import com.dbenavidess.gym_part_1.domain.model.User;
import com.dbenavidess.gym_part_1.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest()
public class TraineeServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TraineeService traineeService;

    @Test
    public void testCreateTraineeSuccess() {
        // Arrange
        User user = new User("Daniel","Benavides",true, userRepository);
        Trainee trainee = new Trainee("my house", LocalDate.of(1997,7,19), user);

        Trainee createdTrainee = traineeService.createTrainee(trainee);

        User user2 = new User("Daniel","Benavides",true, userRepository);
        Trainee trainee2 = new Trainee("my house", LocalDate.of(1997,7,19), user2);

        Trainee createdTrainee2 = traineeService.createTrainee(trainee2);

        // Assert
        assertNotNull(createdTrainee);
        assertNotNull(createdTrainee.getId());
        assertEquals("Daniel" + "." + "Benavides",createdTrainee.getUser().getUsername());
        assertEquals("Daniel" + "." + "Benavides1",trainee2.getUser().getUsername());
    }

    @Test
    public void testCreateTraineeFailure() {
        // Arrange
        Trainee trainee = new Trainee("address",LocalDate.now(), null);

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
        Trainee trainee = new Trainee("my house", LocalDate.of(1997,7,19), user);

        Trainee createdTrainee = traineeService.createTrainee(trainee);

        // Act

        Trainee updatedTrainee = new Trainee("new address",trainee.getDateOfBirth(),user);
        traineeService.updateTrainee(updatedTrainee);

        // Assert
        assertNotNull(updatedTrainee);
        assertNotEquals(trainee, traineeService.getTrainee(updatedTrainee.getId()));
        assertEquals(traineeService.getTrainee(updatedTrainee.getId()).getAddress(), "new address" );
    }

    @Test
    public void testGetTraineeFound() {
        // Arrange
        User user = new User("Daniel","Benavides",true, userRepository);
        Trainee trainee = new Trainee("my house", LocalDate.of(1997,7,19), user);

        Trainee createdTrainee = traineeService.createTrainee(trainee);

        // Act
        Trainee foundTrainee = traineeService.getTrainee(trainee.getId());

        // Assert
        assertNotNull(foundTrainee);
        assertEquals(trainee.getId(), foundTrainee.getId());
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
        Trainee trainee = new Trainee("my house", LocalDate.of(1997,7,19), user);

        Trainee createdTrainee = traineeService.createTrainee(trainee);

        // Act
        traineeService.deleteTrainee(trainee.getId());

        // Assert
        assertNull(traineeService.getTrainee(trainee.getId()));

    }

}
