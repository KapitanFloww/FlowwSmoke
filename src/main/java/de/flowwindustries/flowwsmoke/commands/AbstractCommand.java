package de.flowwindustries.flowwsmoke.commands;

import de.flowwindustries.flowwsmoke.utils.exceptions.InsufficientPermissionException;
import de.flowwindustries.flowwsmoke.utils.messages.PlayerMessage;
import lombok.extern.java.Log;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Abstract class for handling player or console commands.
 */
@Log
public abstract class AbstractCommand implements CommandExecutor {

    public static final String INVALID_ARGUMENTS = "Invalid arguments";

    private final String permission;

    public AbstractCommand(String permission) {
        this.permission = permission;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //If sender is player
        if (sender instanceof Player) {
            Player player = (Player) sender;
            try {
                if (!player.hasPermission(permission)) {
                    throw new InsufficientPermissionException(player, permission);
                }
                return playerCommand(player, args);
            } catch (InsufficientPermissionException ex) {
                PlayerMessage.error(ex.getMessage(), player);
            }
        }

        //If sender is not player
        return consoleCommand(args);
    }

    /**
     * Abstract template for player commands.
     *
     * @param player the executing player
     * @param args command arguments
     * @return  {@code true} if the command executed successful, otherwise {@code false}
     */
    protected abstract boolean playerCommand(Player player, String[] args);

    /**
     * Abstract template for console commands.
     * @param args command arguments
     * @return {@code true} if the command executed successful, otherwise {@code false}
     */
    protected abstract boolean consoleCommand(String[] args);
}

