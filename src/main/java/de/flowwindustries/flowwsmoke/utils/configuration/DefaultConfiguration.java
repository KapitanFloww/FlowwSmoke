package de.flowwindustries.flowwsmoke.utils.configuration;

import org.bukkit.configuration.file.FileConfiguration;

public abstract class DefaultConfiguration {

    public static final String PREFIX_KEY = "prefix";

    public static void setupDefaultConfiguration(FileConfiguration configuration) {
        //Setup values
        configuration.addDefault(PREFIX_KEY, "§7[§aFloww§4Smoke§7]");

        //Save configuration
        configuration.options().copyDefaults(true);
    }
}
