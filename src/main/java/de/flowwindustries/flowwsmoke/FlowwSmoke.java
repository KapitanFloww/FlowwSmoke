package de.flowwindustries.flowwsmoke;

import de.flowwindustries.flowwsmoke.commands.SmokeCommand;
import de.flowwindustries.flowwsmoke.service.SmokeLocationService;
import de.flowwindustries.flowwsmoke.service.impl.SmokeLocationIOServiceImpl;
import de.flowwindustries.flowwsmoke.service.impl.SmokeLocationServiceImpl;
import de.flowwindustries.flowwsmoke.utils.configuration.DefaultConfiguration;
import lombok.Getter;
import lombok.extern.java.Log;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.Random;
import java.util.function.Consumer;

@Log
public final class FlowwSmoke extends JavaPlugin {

    public static final String PREFIX = "§7[§aFloww§4Smoke§7] ";
    private static final String SMOKE_PERMISSION = "floww.smoke";
    private static final Random RANDOM = new Random();

    @Getter
    private static FlowwSmoke instance;

    @Getter
    private FileConfiguration configuration;

    private SmokeLocationService smokeLocationService;

    @Override
    public void onEnable() {
        instance = this;

        setupConfig();
        setupServices();
        setupCommands();

        // Schedule the main plugin task
        scheduleSmokeTask();

        String pluginVersion = instance.getDescription().getVersion();
        log.info(PREFIX + "Initialization complete. Running version: " + pluginVersion);
    }

    @Override
    public void onDisable() {
    }

    private void setupConfig() {
        this.configuration = getConfig();
        DefaultConfiguration.setupDefaultConfiguration(configuration);
        instance.saveConfig();
    }

    private final Consumer<Runnable> persistTaskExecutor = runnable -> {
        Bukkit.getScheduler().runTaskAsynchronously(this, runnable);
    };

    private void setupServices() {
        String dataFilePath = "plugins/FlowwSmoke/smoke-locations.dat";
        this.smokeLocationService = new SmokeLocationServiceImpl(new SmokeLocationIOServiceImpl(dataFilePath, persistTaskExecutor));
    }

    private void setupCommands() {
        Objects.requireNonNull(getCommand("smoke")).setExecutor(new SmokeCommand(SMOKE_PERMISSION, this.smokeLocationService));
    }

    private void scheduleSmokeTask() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(FlowwSmoke.getInstance(),
                () -> smokeLocationService.getAll(null).forEach(smokeLocation -> {

                    World world = Bukkit.getWorld(smokeLocation.getWorldName());
                    if(world == null) {
                        throw new IllegalStateException("World %s is not loaded!".formatted(smokeLocation.getWorldName()));
                    }
                    Location location = new Location(world, smokeLocation.getX(), smokeLocation.getY(), smokeLocation.getZ());

                    // TODO unloading
                    // TODO work with noise map

                    if(RANDOM.nextInt(50) % 3 == 0) {
                        return;
                    }
                    // TODO custom intensity
                    if(!(RANDOM.nextInt(50) % 3 == 0)) {
                        Objects.requireNonNull(world).spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, location, 0, 1, 10.0d, 0.0d, 0.01d);
                        return;
                    }
                    if(!(RANDOM.nextInt(50) % 2 == 0)) {
                        Objects.requireNonNull(world).spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, location, 0, 0.0d, 10.0d, 0.5d, 0.01d);
                        return;
                    }
                    if(!(RANDOM.nextInt(50) % 4 == 0)) {
                        Objects.requireNonNull(world).spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, location, 0, -0.4d, 10.0d, -1, 0.01d);
                    }
                }), 0L, 7L);
    }
}
