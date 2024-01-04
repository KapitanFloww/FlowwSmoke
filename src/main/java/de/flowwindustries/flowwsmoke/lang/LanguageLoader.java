package de.flowwindustries.flowwsmoke.lang;

import lombok.extern.java.Log;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.nio.file.Path;

@Log
public final class LanguageLoader {

    private static YamlConfiguration languageConfig;

    private LanguageLoader() {

    }

    public static void init(Path languageFile) {
        try {
            languageConfig = new YamlConfiguration();
            languageConfig.load(languageFile.toFile());
            log.info("Loaded language: %s".formatted(languageFile.toFile().getPath()));
        } catch (IOException | InvalidConfigurationException ex) {
            log.severe("Could not read language file: %s: %s".formatted(languageFile, ex.getMessage()));
        }
    }

    public static String getMessage(String key) {
        if (languageConfig == null) {
            throw new IllegalStateException("Translation not initialized yet!");
        }
        return languageConfig.getString(key, "Translation missing for key: %s".formatted(key));
    }
}
