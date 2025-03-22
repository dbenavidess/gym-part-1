package com.dbenavidess.gym_part_1.infrastructure.controller;

import com.dbenavidess.gym_part_1.infrastructure.repository.InMemory.implementation.InMemoryStorage;
import com.dbenavidess.gym_part_1.infrastructure.repository.InMemory.implementation.StorageSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.nio.file.Path;

@Controller
public class SaveController {

    private final StorageSerializer serializer;

    @Autowired
    private InMemoryStorage storage;

    @Value("${application.storagePath}")
    private Path path;

    public SaveController() {
        serializer = new StorageSerializer(path);
    }

    @GetMapping
    public void save(){
        serializer.serializeStorage(storage);

    }

}
