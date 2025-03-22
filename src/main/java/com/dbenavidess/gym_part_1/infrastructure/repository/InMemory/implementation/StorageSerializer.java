package com.dbenavidess.gym_part_1.infrastructure.repository.InMemory.implementation;

import java.io.*;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;

public class StorageSerializer {

    Path path;

    public StorageSerializer(Path path) {
        this.path = path;
    }

    public void serializeStorage(InMemoryStorage storage) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path.toString()))) {
            out.writeObject(storage.getStorage());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, Map<UUID,?>> deserializeStorage(){
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toString()))) {
            Object read = ois.readObject();
            if(read != null){
                return (Map<String, Map<UUID,?>>) read;
            }
            return null;

        }catch (IOException | ClassNotFoundException e){
            return null;
        }

    }


}
