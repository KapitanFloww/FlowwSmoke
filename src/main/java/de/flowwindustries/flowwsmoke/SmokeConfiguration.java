package de.flowwindustries.flowwsmoke;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;

public class SmokeConfiguration {

    private final FileConfiguration fileConfiguration;

    public SmokeConfiguration(FileConfiguration fileConfiguration) {
        this.fileConfiguration = Objects.requireNonNull(fileConfiguration);
    }

    public String getPrefix() {
        return fileConfiguration.getString("prefix");
    }

    public String getInsufficientPermissionsMessage() {
        return fileConfiguration.getString("messages.insufficient-permission");
    }
}
