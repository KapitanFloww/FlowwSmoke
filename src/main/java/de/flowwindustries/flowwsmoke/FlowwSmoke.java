package de.flowwindustries.flowwsmoke;

import de.flowwindustries.flowwsmoke.commands.SmokeCommand;
import de.flowwindustries.flowwsmoke.configuration.DefaultConfiguration;
import lombok.Getter;
import lombok.extern.java.Log;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

@Log
public final class FlowwSmoke extends JavaPlugin {

    public static final String PREFIX = "§7[§aFloww§4Smoke§7] ";

    @Getter
    private static FlowwSmoke instance;
    @Getter
    private FileConfiguration configuration;

    @Override
    public void onEnable() {
        instance = this;

        setupConfig();
        setupCommands();

        String pluginVersion = instance.getDescription().getVersion();
        log.info(PREFIX + "Initialization complete. Running version: " + pluginVersion);
    }

    @Override
    public void onDisable() {
        SmokeService.persistLocations();
    }

    private void setupConfig() {
        this.configuration = getConfig();
        DefaultConfiguration.setupDefaultConfiguration(configuration);
        instance.saveConfig();
    }

    private void setupCommands() {
        getCommand("smoke").setExecutor(new SmokeCommand("floww.smoke"));
    }
}
