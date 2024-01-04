package de.flowwindustries.flowwsmoke;

import lombok.extern.java.Log;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

@Log
public class SmokeConfiguration {

    private final FileConfiguration fileConfiguration;

    public SmokeConfiguration(FileConfiguration fileConfiguration) {
        this.fileConfiguration = Objects.requireNonNull(fileConfiguration);
        validateConfiguration(fileConfiguration);
    }

    public String getPrefix() {
        return fileConfiguration.getString("prefix");
    }

    public String getLanguage() {
        return fileConfiguration.getString("lang");
    }

    public int getFallbackFrequency() {
        return fileConfiguration.getInt("fallback-poll-frequency");
    }

    private void validateConfiguration(FileConfiguration fileConfiguration) {
        try (final InputStreamReader in = new InputStreamReader(Objects.requireNonNull(SmokeConfiguration.class.getResourceAsStream("/config.yml")))) {
            final var expectedConfiguration = new YamlConfiguration();
            expectedConfiguration.load(in);

            // Check if any key is missing in actual configuration
            final var missingKeys = new ArrayList<String>();
            final var expectedKeys = expectedConfiguration.getKeys(true);
            final var actualKeys = fileConfiguration.getKeys(true);
            for (String expectedKey : expectedKeys) {
                if (!actualKeys.contains(expectedKey)) {
                    log.severe("Configuration is missing key: " + expectedKey);
                    missingKeys.add(expectedKey);
                }
            }
            if (!missingKeys.isEmpty()) {
                throw new IllegalStateException("Configuration is missing keys: %s".formatted(String.join(", ", missingKeys)));
            }
        } catch (IOException | InvalidConfigurationException ex) {
            log.severe("Could not load config.yml from resource folder: " + ex);
        }
    }
}
