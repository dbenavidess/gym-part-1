package com.dbenavidess.gym_part_1.infrastructure.controller;

import com.dbenavidess.gym_part_1.infrastructure.repository.InMemory.implementation.InMemoryStorage;
import com.dbenavidess.gym_part_1.infrastructure.repository.InMemory.implementation.StorageSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;

@ConditionalOnProperty(name = "spring.application.persistence", havingValue = "inmemory")
@RestController
public class SaveController {

    private final StorageSerializer serializer;

    @Autowired
    private InMemoryStorage storage;

    Path path;

    public SaveController(@Value("${application.storagePath}") Path path) {
        this.path = path;
        serializer = new StorageSerializer(path);
    }

    @GetMapping("/save")
    public void save(){
        serializer.serializeStorage(storage);

    }

}
