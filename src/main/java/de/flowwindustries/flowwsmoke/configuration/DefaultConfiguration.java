package de.flowwindustries.flowwsmoke.configuration;

import org.bukkit.configuration.file.FileConfiguration;

public abstract class DefaultConfiguration {

    public static final String PARTICLES_AMOUNT_KEY = "particles.amount";

    public static void setupDefaultConfiguration(FileConfiguration configuration) {
        //Setup values
        configuration.addDefault(PARTICLES_AMOUNT_KEY, 3);

        //Save configuration
        configuration.options().copyDefaults(true);
    }
}
