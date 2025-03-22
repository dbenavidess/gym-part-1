package com.dbenavidess.gym_part_1.infrastructure.repository.InMemory.implementation;

import com.dbenavidess.gym_part_1.domain.model.*;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class InMemoryStorage {

    @Setter
    @Getter
    private Map<String, Map> storage = new HashMap<>();

    @Value("${application.storagePath}")
    private Path path;

    @Autowired
    public void setTrainerStorage(Map<UUID,Trainer> trainerStorage){
        storage.put(Trainer.class.getName(),trainerStorage);
    }
    @Autowired
    public void setTraineeStorage(Map<UUID, Trainee> traineeStorage){
        storage.put(Trainee.class.getName(),traineeStorage);
    }
    @Autowired
    public void setTrainingStorage(Map<UUID, Training> trainingStorage){
        storage.put(Training.class.getName(),trainingStorage);
    }
    @Autowired
    public void setUserStorage(Map<UUID, User> userStorage){
        storage.put(Training.class.getName(),userStorage);
    }

    @PostConstruct
    public void customInit() {
        StorageSerializer serializer = new StorageSerializer(path);
        Map<String, Map<UUID,?>> map = serializer.deserializeStorage();
        if (map == null){
            return;
        }
        deserializeTrainers(map);
        deserializeTrainees(map);
        deserializeTrainings(map);
    }

    private void deserializeTrainings(Map<String, Map<UUID,?>> map) {
        map.computeIfPresent(Training.class.getName(),(key,val)->{
            Map<UUID,Training> trainingMap = (Map<UUID,Training>) val;
            trainingMap.values()
                    .forEach((t->{
                        Trainer trainer = (Trainer) storage.get(Trainer.class.getName()).get(t.getTrainer().getId());
                        Trainee trainee = (Trainee) storage.get(Trainee.class.getName()).get(t.getTrainer().getId());
                        UUID id  = t.getId();
                        String name = t.getName();
                        TrainingType type = t.getType();
                        Date date = t.getDate();
                        int duration = t.getDuration();
                        Training training = new Training(id,
                                trainer,
                                trainee,
                                name,
                                type,
                                date,
                                duration);
                        storage.get(Trainee.class.getName()).put(training.getId(),training);
                    }));
            return null;
        });
    }

    private void deserializeTrainees(Map<String, Map<UUID,?>> map) {
        map.computeIfPresent(Trainee.class.getName(),(key,val)->{
            Map<UUID,Trainee> traineeMap = (Map<UUID,Trainee>) val;
            traineeMap.values()
                    .forEach((t ->{
                        User user = t.getUser();
                        storage.get(User.class.getName()).put(user.getId(),user);
                        UUID id = t.getId();
                        String address = t.getAddress();
                        Date dateOfBirth = t.getDateOfBirth();
                        Trainee trainee = new Trainee(
                                id,
                                user,
                                address,
                                dateOfBirth);
                        storage.get(Trainee.class.getName()).put(trainee.getId(),trainee);

                    }));
            return null;
        });
    }

    private void deserializeTrainers(Map<String, Map<UUID,?>> map) {
        map.computeIfPresent(Trainer.class.getName(),(key,val)->{
            Map<UUID,Trainer> trainerMap = (Map<UUID,Trainer>) val;
            trainerMap.values()
                    .forEach((t ->{
                        User user = t.getUser();
                        storage.get(User.class.getName()).put(user.getId(),user);
                        UUID id = t.getId();
                        TrainingType specialization = t.getSpecialization();
                        Trainer trainer = new Trainer(
                                id,
                                user,
                                specialization
                        );
                        storage.get(Trainer.class.getName()).put(trainer.getId(),trainer);

                    }));
            return null;
        });
    }

}
