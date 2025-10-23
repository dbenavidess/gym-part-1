package com.dbenavidess.gym_part_1.service;

import com.dbenavidess.gym_part_1.domain.model.Trainee;
import com.dbenavidess.gym_part_1.domain.model.Trainer;
import com.dbenavidess.gym_part_1.domain.model.Training;
import com.dbenavidess.gym_part_1.domain.model.TrainingType;
import com.dbenavidess.gym_part_1.domain.repository.TrainingRepository;
import com.dbenavidess.gym_part_1.domain.repository.TrainingTypeRepository;
import com.dbenavidess.gym_part_1.infrastructure.request.workload.WorkloadRequest;
import com.dbenavidess.gym_part_1.messaging.WorkloadMessagePublisher;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Service
public class TrainingService {

    private final TrainingRepository repository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final TraineeService traineeService;
    private final TrainerService trainerService;

    private final WorkloadMessagePublisher publisher;


    public TrainingService(TrainingRepository repository, TrainingTypeRepository trainingTypeRepository, TraineeService traineeService, TrainerService trainerService, WorkloadMessagePublisher publisher) {
        this.repository = repository;
        this.trainingTypeRepository = trainingTypeRepository;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.publisher = publisher;
    }

    public Training createTraining(String name, Date date, Integer duration, String trainingTypeName, String trainerUsername, String traineeUsername){

        Trainer trainer = trainerService.getTrainerByUsername(trainerUsername);
        Trainee trainee = traineeService.getTraineeByUsername(traineeUsername);
        TrainingType trainingType = trainingTypeRepository.getByName(trainingTypeName);

        Training training = repository.createTraining(new Training(trainer,trainee,name,trainingType, date, duration));
        if(training == null){
            throw new RuntimeException("Invalid Training");
        }

        // Prepare WorkloadRequest
        WorkloadRequest request = buildWorkloadRequest(training,"ADD");

        // Send to secondary service through
        sendWorkloadUpdate(request);

        return training;
    }

    public void deleteTraining(UUID id) {

        Training training = getTraining(id);

        // Prepare WorkloadRequest
        WorkloadRequest request = buildWorkloadRequest(training,"DELETE");

        // Send to secondary service
        sendWorkloadUpdate(request);
        repository.deleteTraining(id);
    }

    public Training getTraining(UUID id){
        return repository.getTraining(id);
    }

    public List<Training> searchTrainings(String trainerUsername, Date from, Date to, String traineeUsername, String trainingTypeName){

        Trainee trainee = null;
        Trainer trainer = null;
        TrainingType type = null;
        if(traineeUsername != null && !traineeUsername.isEmpty()) {
            trainee = traineeService.getTraineeByUsername(traineeUsername);
        }
        if(trainerUsername != null && !trainerUsername.isEmpty()){
            trainer = trainerService.getTrainerByUsername(trainerUsername);
        }
        if(trainingTypeName != null && !trainingTypeName.isEmpty()){
            type = trainingTypeRepository.getByName(trainingTypeName);
        }

        return repository.searchTrainings(from,to,trainer,trainee,type);
    }

    public void sendWorkloadUpdate(WorkloadRequest request) {
        publisher.sendWorkloadEvent(request);
    }

    private WorkloadRequest buildWorkloadRequest(Training training, String actionType) {
        WorkloadRequest request = new WorkloadRequest();
        request.setTrainerUsername(training.getTrainer().getUser().getUsername());
        request.setTrainerFirstName(training.getTrainer().getUser().getFirstName());
        request.setTrainerLastName(training.getTrainer().getUser().getLastName());
        request.setIsActive(training.getTrainer().getUser().getIsActive());
        request.setTrainingDate(training.getDate().toLocalDate());
        request.setTrainingDuration(training.getDuration());
        request.setActionType(actionType);
        return request;
    }

}