package com.dbenavidess.gym_part_1.integrationTest.steps;

import com.dbenavidess.gym_part_1.service.TrainingService;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.dbenavidess.gym_part_1.service.UserService;
import com.dbenavidess.gym_part_1.service.TrainerService;
import com.dbenavidess.gym_part_1.service.TraineeService;
import com.dbenavidess.gym_part_1.config.security.service.JwtService;
import com.dbenavidess.gym_part_1.config.security.service.BruteForceSecurityService;
import com.dbenavidess.gym_part_1.domain.repository.TrainingTypeRepository;
import com.dbenavidess.gym_part_1.domain.repository.UserRepository;
import com.dbenavidess.gym_part_1.domain.util.PasswordEncryptionProvider;

@CucumberContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class CucumberSpringConfiguration {

    @MockitoBean protected UserService userService;
    @MockitoBean protected TrainerService trainerService;
    @MockitoBean protected TraineeService traineeService;
    @MockitoBean protected TrainingService trainingService;

    @MockitoBean protected JwtService jwtService;
    @MockitoBean protected BruteForceSecurityService bruteForceSecurityService;

    @MockitoBean protected TrainingTypeRepository trainingTypeRepository;
    @MockitoBean protected UserRepository userRepository;
    @MockitoBean protected PasswordEncryptionProvider passwordEncryptionProvider;

    @MockitoBean protected AuthenticationManager authenticationManager;
}