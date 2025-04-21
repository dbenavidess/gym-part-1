package com.dbenavidess.gym_part_1.infrastructure.repository.InMemory.implementation;

import com.dbenavidess.gym_part_1.domain.model.*;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@ConditionalOnProperty(name = "spring.application.persistence", havingValue = "inmemory")
public class InMemoryStorage {

    @Setter
    @Getter
    private Map<String, Map> storage = new HashMap<>();

    @Value("${application.storagePath}")
    private Path path;

    public InMemoryStorage() {
        storage.put(Trainer.class.getName(), new HashMap<UUID, Trainer>());
        storage.put(Trainee.class.getName(), new HashMap<UUID, Trainee>());
        storage.put(Training.class.getName(), new HashMap<UUID, Training>());
        storage.put(User.class.getName(), new HashMap<UUID, User>());
    }

    @Bean("trainerStorage")
    public Map<UUID, Trainer> trainerStorage(){
        return storage.get(Trainer.class.getName());
    }

    @Bean("traineeStorage")
    public Map<UUID, Trainee> traineeStorage(){
        return storage.get(Trainee.class.getName());
    }

    @Bean("trainingStorage")
    public Map<UUID, Training> trainingStorage(){
        return storage.get(Training.class.getName());
    }

    @Bean("userStorage")
    public Map<UUID, User> userStorage(){
        return storage.get(User.class.getName());
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
                                address,
                                dateOfBirth,
                                user);
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
