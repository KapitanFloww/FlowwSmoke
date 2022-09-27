package de.flowwindustries.flowwsmoke.commands;

import de.flowwindustries.flowwsmoke.service.SmokeLocationService;
import de.flowwindustries.flowwsmoke.utils.exceptions.InsufficientPermissionException;
import de.flowwindustries.flowwsmoke.utils.messages.PlayerMessage;
import lombok.extern.java.Log;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.Objects;

/**
 * Smoke Command to create small smokers.
 * /smoke - create new smoke
 * /smoke list [world] - list all smokes
 * /smoke remove (id / [world]) - remove smoke with id or all smokes
 */
@Log
public class SmokeCommand implements CommandExecutor {

    private static final String INVALID_ARGUMENTS = "Invalid arguments at index: %s";
    private static final String INVALID_ARGUMENTS_LENGTH = "Invalid argument length: %s";

    private final SmokeLocationService smokeLocationService;
    private final String permission;

    public SmokeCommand(String permission, SmokeLocationService smokeLocationService) {
        this.permission = Objects.requireNonNull(permission);
        this.smokeLocationService = Objects.requireNonNull(smokeLocationService);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player player) {
            try {
                if (!player.hasPermission(permission)) {
                    throw new InsufficientPermissionException(player, permission);
                }
                return playerCommand(player, args);
            } catch (Exception ex) {
                PlayerMessage.warn(ex.getMessage(), player);
            }
        }
        return consoleCommand(args);
    }

    private boolean playerCommand(Player player, String[] args) {
        switch (args.length) {
            case 0 -> executeSmokeCreation(player); // smoke
            case 1 -> {
                switch (args[0].toLowerCase(Locale.getDefault())) {
                    case "help" -> executeHelp(player); // smoke help
                    case "list" -> executeListAll(player, null); // smoke list
                    case "remove" -> executeRemoveAll(player, null); // smoke remove
                    default -> throw new IllegalArgumentException(INVALID_ARGUMENTS.formatted(0));
                }
            }
            case 2 -> {
                switch (args[0].toLowerCase(Locale.getDefault())) {
                    case "list" -> executeListAll(player, args[1]);
                    case "remove" -> {
                        try {
                            // Try delete by id - if args[1] is integer
                            int id = Integer.parseInt(args[1]);
                            executeRemoveSmoke(player, id); // smoke remove [id]

                        } catch (NumberFormatException ex) {
                            // Else delete all by world
                            executeRemoveAll(player, args[1]); // smoke remove [world]
                        }
                    }
                    default -> throw new IllegalArgumentException(INVALID_ARGUMENTS.formatted(0));
                }
            }
            default -> throw new IllegalArgumentException(INVALID_ARGUMENTS_LENGTH.formatted(args.length));
        }
        return true;
    }

    private boolean consoleCommand(String[] args) {
        Bukkit.getConsoleSender().sendMessage("Sorry. Command can only be used in-game");
        return false;
    }

    private void executeSmokeCreation(Player player) {
        Block targetBlock = player.getTargetBlockExact(5);
        if(targetBlock == null) {
            throw new IllegalArgumentException("Must be looking at a block close-by");
        }
        int x = targetBlock.getX();
        int y = targetBlock.getY();
        int z = targetBlock.getZ();
        Location targetLocation = new Location(targetBlock.getWorld(), (x+0.5d), (y+0.5d), (z+0.5d));
        if(targetLocation.getWorld() == null) {
            throw new IllegalStateException("World must not be null");
        }
        int id = smokeLocationService.addSmoke(targetLocation);
        PlayerMessage.success("Placed smoke (%s) at [%s, %s, %s]".formatted(id, targetLocation.getX(), targetLocation.getY(), targetLocation.getZ()), player);
    }

    private void executeHelp(Player player) {
        PlayerMessage.info("-- Smoke Help --", player);
        PlayerMessage.info("/smoke - create new smoke at target location", player);
        PlayerMessage.info("/smoke list [world] - list all smokes [in world]", player);
        PlayerMessage.info("/smoke remove (id / [world]) - remove smoke with id or all smokes [in world]", player);
    }

    private void executeListAll(Player player, String worldName) {
        if(worldName == null) {
            PlayerMessage.info("Listing all smokes:", player);
        } else {
            PlayerMessage.info("Listing all smokes in world %s:".formatted(worldName), player);
        }
        smokeLocationService.getAll(worldName).forEach(smokeLocation -> {
            var location = smokeLocation.getLocation();
            PlayerMessage.info("Smoke (%s) - at [%s, %s, %s]".formatted(smokeLocation.getId(), location.getX(), location.getY(), location.getZ()), player);
        });
    }

    private void executeRemoveSmoke(Player player, Integer id) {
        smokeLocationService.deleteSmoke(id);
        PlayerMessage.success("Deleted smoke: %s".formatted(id), player);
    }

    private void executeRemoveAll(Player player, String worldName) {
        smokeLocationService.deleteAll(worldName);
        if(worldName == null) {
            PlayerMessage.success("Deleted all smokes", player);
        } else {
            PlayerMessage.success("Deleted all smokes in world %s".formatted(worldName), player);
        }
    }
}