package de.flowwindustries.flowwsmoke.service.impl;

import de.flowwindustries.flowwsmoke.FlowwSmoke;
import de.flowwindustries.flowwsmoke.domain.SmokeLocation;
import de.flowwindustries.flowwsmoke.service.SmokeLocationIOService;
import lombok.extern.java.Log;
import org.bukkit.Bukkit;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Service implementation of {@link SmokeLocationIOServiceImpl}.
 */
@Log
public class SmokeLocationIOServiceImpl implements SmokeLocationIOService {

    private static final File LOCATIONS_DATA_FILE = Path.of("plugins", FlowwSmoke.getInstance().getName(), "smoke-locations.dat").toFile();

    @Override
    public void persistLocations(List<SmokeLocation> locations) {
        // Persist asynchronously
        var persistTask = new Runnable() {
            @Override
            public void run() {
                try {
                    if(!LOCATIONS_DATA_FILE.exists()) {
                        LOCATIONS_DATA_FILE.createNewFile();
                    }
                    BukkitObjectOutputStream out = new BukkitObjectOutputStream(new GZIPOutputStream(new FileOutputStream(LOCATIONS_DATA_FILE)));
                    out.writeObject(locations);
                    out.close();
                } catch (IOException ex) {
                    log.warning("Error while persisting file: " + ex.getMessage());
                }
            }
        };
        Bukkit.getScheduler().runTaskAsynchronously(FlowwSmoke.getInstance(), persistTask);
    }

    @Override
    public synchronized List<SmokeLocation> loadLocations() throws RuntimeException {
        try {
            if(!LOCATIONS_DATA_FILE.exists()) {
                log.warning("Did not found data file");
                return new ArrayList<>();
            }
            BukkitObjectInputStream in = new BukkitObjectInputStream(new GZIPInputStream(new FileInputStream(LOCATIONS_DATA_FILE)));
            var result = (List<SmokeLocation>) in.readObject();
            log.info("Loaded %s smoke locations from file".formatted(result.size()));
            return result;
        } catch (IOException | ClassNotFoundException ex) {
            log.warning("Error while loading smoke locations: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}
