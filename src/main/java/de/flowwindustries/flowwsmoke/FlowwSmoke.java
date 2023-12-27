package de.flowwindustries.flowwsmoke;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.flowwindustries.flowwsmoke.commands.SmokeCommand;
import de.flowwindustries.flowwsmoke.service.SmokeLocationIOService;
import de.flowwindustries.flowwsmoke.service.SmokeLocationService;
import de.flowwindustries.flowwsmoke.service.impl.SmokeTaskServiceImpl;
import de.flowwindustries.flowwsmoke.service.impl.SmokeLocationIOJsonServiceImpl;
import de.flowwindustries.flowwsmoke.service.impl.SmokeLocationServiceImpl;
import lombok.Getter;
import lombok.extern.java.Log;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;


@Log
public final class FlowwSmoke extends JavaPlugin {

    public static String pluginPrefix;
    public static String messagesInsufficientPermissions;
    private static final String SMOKE_PERMISSION = "floww.smoke";

    @Getter
    private static FlowwSmoke instance;
    @Getter
    private SmokeConfiguration configuration;

    private SmokeLocationService smokeLocationService;
    private SmokeLocationIOService smokeLocationIOService;
    private SmokeTaskServiceImpl smokeTaskService;

    @Override
    public void onEnable() {
        instance = this;

        // Always save default configuration if not exists
        saveDefaultConfig();

        // Load configuration
        configuration = new SmokeConfiguration(getConfig());
        pluginPrefix = configuration.getPrefix();
        messagesInsufficientPermissions = configuration.getInsufficientPermissionsMessage();


        try {
            setupServices();
        } catch (IOException ex) {
            log.severe(ex.getMessage());
        }

        setupCommands();

        String pluginVersion = instance.getDescription().getVersion();
        log.info("Initialization complete. Running version: " + pluginVersion);
    }

    @Override
    public void onDisable() {
        log.info("Shutting down plugin");
        // Cancel all running tasks of this plugin
        log.config("Cancelling all scheduled tasks");
        smokeTaskService.cancelAll();
    }

    private final Consumer<Runnable> persistTaskExecutor =
            runnable -> Bukkit.getScheduler().runTaskAsynchronously(this, runnable);

    private void setupServices() throws IOException {
        final var mapper = new ObjectMapper();
        final var storageFile = Path.of(getDataFolder().getPath(), "smoke-locations.json").toFile();

        // Create the storage-file if not exists
        if (!storageFile.exists()) {
            log.info("Data file not found. Creating new file");
            storageFile.createNewFile();
            mapper.writeValue(new FileOutputStream(storageFile), new ArrayList<>());
        }

        // Setup services
        smokeLocationIOService = new SmokeLocationIOJsonServiceImpl(storageFile, mapper, persistTaskExecutor);
        smokeTaskService = new SmokeTaskServiceImpl(Bukkit.getScheduler());
        smokeLocationService = new SmokeLocationServiceImpl(smokeLocationIOService, smokeTaskService);

        // Schedule tasks for all existing locations
        final var activeSmokeLocations = Bukkit.getWorlds().stream()
                .flatMap(world -> smokeLocationService.getAll(world.getName()).stream())
                .distinct()
                .toList();
        activeSmokeLocations.forEach(smokeTaskService::scheduleSmokeTask);
    }

    private void setupCommands() {
        Objects.requireNonNull(getCommand("smoke")).setExecutor(new SmokeCommand(SMOKE_PERMISSION, this.smokeLocationService, getConfiguration().getFallbackFrequency()));
    }
}
