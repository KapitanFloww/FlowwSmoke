package de.flowwindustries.flowwsmoke.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.flowwindustries.flowwsmoke.domain.SmokeLocation;
import de.flowwindustries.flowwsmoke.service.SmokeLocationIOService;
import lombok.extern.java.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@Log
public class SmokeLocationIOJsonServiceImpl implements SmokeLocationIOService {

    private final File storageFile;
    private final ObjectMapper objectMapper;
    private final Consumer<Runnable> persistTaskExecutor;

    public SmokeLocationIOJsonServiceImpl(File storageFile, ObjectMapper objectMapper, Consumer<Runnable> persistTaskExecutor) {
        this.storageFile = Objects.requireNonNull(storageFile);
        this.objectMapper = Objects.requireNonNull(objectMapper);
        this.persistTaskExecutor = Objects.requireNonNull(persistTaskExecutor);
    }

    @Override
    public void persistLocations(List<SmokeLocation> locations) {
        final var persistTask = new Runnable() {
            @Override
            public void run() {
                try {
                    log.config("Persisting %s locations".formatted(locations.size()));
                    objectMapper.writeValue(new FileOutputStream(storageFile), locations);
                } catch (IOException ex) {
                    log.severe("Could not persist locations: " + ex.getMessage());
                }
            }
        };
        persistTaskExecutor.accept(persistTask);
    }

    @Override
    public synchronized List<SmokeLocation> loadLocations() {
        try {
            final var locations = objectMapper.readValue(new FileInputStream(storageFile), new TypeReference<List<SmokeLocation>>(){});
            log.info("Loaded %s locations from file: %s".formatted(locations.size(), storageFile.getPath()));
            return locations;
        } catch (IOException ex) {
            log.severe("Could not load locations: " + ex.getMessage());
        }
        return Collections.emptyList();
    }
}
