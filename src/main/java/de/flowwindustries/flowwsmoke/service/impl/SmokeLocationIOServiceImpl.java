package de.flowwindustries.flowwsmoke.service.impl;

import de.flowwindustries.flowwsmoke.domain.SmokeLocation;
import de.flowwindustries.flowwsmoke.service.SmokeLocationIOService;
import lombok.extern.java.Log;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Service implementation of {@link SmokeLocationIOServiceImpl}.
 */
@Log
public class SmokeLocationIOServiceImpl implements SmokeLocationIOService {

    public final File dataFile;
    private final Consumer<Runnable> persistTaskExecutor;

    public SmokeLocationIOServiceImpl(String dataFilePath, Consumer<Runnable> persistTaskExecutor) {
        this.dataFile = Path.of(dataFilePath).toFile();
        this.persistTaskExecutor = persistTaskExecutor;
    }

    @Override
    public void persistLocations(List<SmokeLocation> locations) {
        var persistTask = new Runnable() {
            @Override
            public void run() {
                try {
                    if(!dataFile.exists()) {
                        boolean result = dataFile.createNewFile();
                        log.info("Creating new data file at: %s (%s)".formatted(dataFile.getAbsolutePath(), result));
                    }
                    BukkitObjectOutputStream out = new BukkitObjectOutputStream(new GZIPOutputStream(new FileOutputStream(dataFile)));
                    out.writeObject(locations);
                    out.close();
                    log.info("Successfully persisted smoke locations");
                } catch (IOException ex) {
                    log.warning("Error while persisting file: " + ex.getMessage());
                }
            }
        };
        persistTaskExecutor.accept(persistTask);
    }

    @Override
    public synchronized List<SmokeLocation> loadLocations() throws RuntimeException {
        try {
            if(!dataFile.exists()) {
                log.warning("Did not found data file");
                return new ArrayList<>();
            }
            BukkitObjectInputStream in = new BukkitObjectInputStream(new GZIPInputStream(new FileInputStream(dataFile)));
            var result = (List<SmokeLocation>) in.readObject();
            log.info("Loaded %s smoke locations from file".formatted(result.size()));
            return result;
        } catch (IOException | ClassNotFoundException ex) {
            log.warning("Error while loading smoke locations: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}
