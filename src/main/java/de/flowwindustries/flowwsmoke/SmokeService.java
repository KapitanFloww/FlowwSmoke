package de.flowwindustries.flowwsmoke;

import lombok.extern.java.Log;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@Log
public class SmokeService {

    private static final Path LOCATIONS_DATA_FILE = Path.of("plugins", "FlowwSmoke", "smoke-locations.dat");
    private static final Random random = new Random();

    private static Map<Integer, Location> smokeLocations;
    private static int counter;

    static {
        initializeLocations();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(FlowwSmoke.getInstance(),
                () -> smokeLocations.values().forEach(smokeLocation -> {
                    if(!smokeLocation.getChunk().isEntitiesLoaded()) {
                        return;
                    }
                    // TODO work with noise map
                    if(random.nextInt(50) % 3 == 0) {
                        return;
                    }
                    // TODO custom intensity
                    if(!(random.nextInt(50) % 3 == 0)) {
                        smokeLocation.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, smokeLocation, 0, 1, 10.0d, 0.0d, 0.01d);
                        return;
                    }
                    if(!(random.nextInt(50) % 2 == 0)) {
                        smokeLocation.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, smokeLocation, 0, 0.0d, 10.0d, 0.5d, 0.01d);
                        return;
                    }
                    if(!(random.nextInt(50) % 4 == 0)) {
                        smokeLocation.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, smokeLocation, 0, -0.4d, 10.0d, -1, 0.01d);
                    }
                }), 0l, 7l);
    }

    private static void initializeLocations() {
        try {
            if(!LOCATIONS_DATA_FILE.toFile().exists()) {
                smokeLocations = new ConcurrentHashMap<>();
            }
            BukkitObjectInputStream in = new BukkitObjectInputStream(new GZIPInputStream(new FileInputStream(LOCATIONS_DATA_FILE.toFile())));
            smokeLocations = (Map<Integer, Location>) in.readObject();
            setCounter();
        } catch (IOException | ClassNotFoundException ex) {
            log.warning("Could not initialize smoke locations: " + ex.getMessage());
        }
    }

    private static void setCounter() {
        AtomicInteger temp = new AtomicInteger(0);
        smokeLocations.keySet().forEach(integer -> {
            if(integer > temp.get()) {
                temp.set(integer);
            }
        });
        counter = temp.get() + 1;
    }

    public static void persistLocations() {
        try {
            if(!LOCATIONS_DATA_FILE.toFile().exists()) {
                LOCATIONS_DATA_FILE.toFile().createNewFile();
            }
            BukkitObjectOutputStream out = new BukkitObjectOutputStream(new GZIPOutputStream(new FileOutputStream(LOCATIONS_DATA_FILE.toFile())));
            out.writeObject(smokeLocations);
            out.close();
        } catch (IOException ex) {
            log.warning("Could not persist file: " + ex.getMessage());
        }
    }
}
