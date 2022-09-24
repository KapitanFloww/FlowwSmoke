package de.flowwindustries.flowwsmoke.utils.exceptions;

import de.flowwindustries.flowwsmoke.FlowwSmoke;
import lombok.Getter;
import lombok.extern.java.Log;
import org.bukkit.entity.Player;

import static de.flowwindustries.flowwsmoke.FlowwSmoke.pluginPrefix;

/**
 * {@link RuntimeException} to handle insufficient permissions.
 */
@Log
public class InsufficientPermissionException extends Exception {

    private static final String CONFIG_KEY = "messages.insufficient-permission";
    public static final String CONFIGURATION_KEY_MISSING = "You configuration file is not setup properly! You are missing the key: ";

    @Getter
    private String message;

    /**
     * Display a warning to the sending {@link Player} that required permissions are missing.
     * @param sender the {@link Player} that tried to send the command with insufficient permissions
     */
    public InsufficientPermissionException(Player sender, String permission) {
        String insufficientPermissions = FlowwSmoke.getInstance().getConfiguration().getString(CONFIG_KEY);
        if(insufficientPermissions == null) {
            log.warning(pluginPrefix + CONFIGURATION_KEY_MISSING + CONFIG_KEY);
            return;
        }
        this.message = insufficientPermissions;
        log.warning(String.format("%sPlayer %s tried to use a command without permission: %s", pluginPrefix, sender.getName(), permission));
    }
}
