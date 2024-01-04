package de.flowwindustries.flowwsmoke.commands;

import de.flowwindustries.flowwsmoke.FlowwSmoke;
import de.flowwindustries.flowwsmoke.domain.SmokeLocation;
import de.flowwindustries.flowwsmoke.service.SmokeLocationService;
import de.flowwindustries.flowwsmoke.utils.messages.PlayerMessage;
import de.flowwindustries.flowwsmoke.utils.parsing.SpigotStringParser;
import lombok.extern.java.Log;
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
 * /smoke remove (id) - remove smoke with id or all smokes
 * /smoke remove all [world] - remove all smokes
 */
@Log
public class SmokeCommand implements CommandExecutor {

    public static final String UNKNOWN_ARGUMENT = "Unknown Argument: %s";
    public static final String UNKNOWN_ARGUMENTS_LENGTH = "Unknown argument length: %s";

    public static final String MSG_HELP_TITLE = "-- Smoke Help --";
    public static final String MSG_HELP_1 = "/smoke - create new smoke at target location";
    public static final String MSG_HELP_2 = "/smoke add (frequency in server-ticks) - create new smoke at target location with custom spawn-frequency";
    public static final String MSG_HELP_3 = "/smoke list [world] - list all smokes [in world]";
    public static final String MSG_HELP_4 = "/smoke remove (id) - remove smoke with id";
    public static final String MSG_HELP_5 = "/smoke remove all [world] - remove all smokes [in world]";
    public static final String MSG_HELP_6 = "-- [] - optional args, () - required args --";

    private final SmokeLocationService smokeLocationService;
    private final Integer fallbackFrequency;
    private final String permission;

    public SmokeCommand(String permission, SmokeLocationService smokeLocationService, Integer fallbackFrequency) {
        this.permission = Objects.requireNonNull(permission);
        this.smokeLocationService = Objects.requireNonNull(smokeLocationService);
        this.fallbackFrequency = Objects.requireNonNull(fallbackFrequency);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player player) {
            try {
                if (!player.hasPermission(permission)) {
                    throw new IllegalArgumentException(FlowwSmoke.getInstance().getConfiguration().getInsufficientPermissionsMessage());
                }
                return playerCommand(player, args);
            } catch (Exception ex) {
                PlayerMessage.warn(ex.getMessage(), player);
            }
        } else {
            return consoleCommand(args);
        }
        return false;
    }

    private boolean playerCommand(Player player, String[] args) {
        switch (args.length) {
            case 0 -> executeSmokeCreation(player); // smoke
            case 1 -> {
                switch (args[0].toLowerCase(Locale.getDefault())) {
                    case "help" -> executeHelp(player); // smoke help
                    case "list" -> executeListAll(player, null); // smoke list
                    default -> throw new IllegalArgumentException(UNKNOWN_ARGUMENT.formatted(args[0]));
                }
            }
            case 2 -> {
                switch (args[0].toLowerCase(Locale.getDefault())) {
                    case "add" -> executeSmokeCreation(player, SpigotStringParser.parseIntSafe(args[1]));
                    case "list" -> executeListAll(player, args[1]);
                    case "remove" -> {
                        try {
                            // Try delete by id - if args[1] is integer
                            int id = Integer.parseInt(args[1]);
                            executeRemoveSmoke(player, id); // smoke remove [id]
                        } catch (NumberFormatException ex) {
                            if(!args[1].equalsIgnoreCase("all")) {
                                throw new IllegalArgumentException(UNKNOWN_ARGUMENT.formatted(args[1]));
                            }
                            executeRemoveAll(player, null); // smoke remove all
                        }
                    }
                    default -> throw new IllegalArgumentException(UNKNOWN_ARGUMENT.formatted(args[0]));
                }
            }
            case 3 -> {
                if(!(args[0].equalsIgnoreCase("remove") && args[1].equalsIgnoreCase("all"))) {
                    throw new IllegalArgumentException(UNKNOWN_ARGUMENT.formatted(args[0] + " " + args[1]));
                }
                executeRemoveAll(player, args[2]); // smoke remove all <world>
            }
            default -> throw new IllegalArgumentException(UNKNOWN_ARGUMENTS_LENGTH.formatted(args.length));
        }
        return true; //remove all <world>
    }

    private boolean consoleCommand(String[] args) {
        log.info("Sorry. Command can only be used in-game");
        return false;
    }

    private void executeSmokeCreation(Player player) {
        executeSmokeCreation(player, fallbackFrequency);
    }

    private void executeSmokeCreation(Player player, int frequency) {
        if (frequency < 1) {
            throw new IllegalArgumentException("Spawn-Frequency must not be < 1");
        }
        Block targetBlock = player.getTargetBlockExact(5);
        if(targetBlock == null) {
            throw new IllegalArgumentException("Must be looking at a block close-by");
        }
        double x = targetBlock.getX() + 0.5d;
        double y = targetBlock.getY() + 0.5d;
        double z = targetBlock.getZ() + 0.5d;
        String worldName = targetBlock.getWorld().getName();

        var location = new SmokeLocation()
                .withFrequency(frequency)
                .withWorldName(worldName)
                .withX(x)
                .withY(y)
                .withZ(z);
        int id = smokeLocationService.addSmoke(location);
        PlayerMessage.success("Placed smoke (%s) at [%s, %s, %s]. Spawn-Frequency: %s".formatted(id, x, y, z, frequency), player);
    }

    private void executeHelp(Player player) {
        PlayerMessage.info(MSG_HELP_TITLE, player);
        PlayerMessage.info(MSG_HELP_1, player);
        PlayerMessage.info(MSG_HELP_2, player);
        PlayerMessage.info(MSG_HELP_3, player);
        PlayerMessage.info(MSG_HELP_4, player);
        PlayerMessage.info(MSG_HELP_5, player);
        PlayerMessage.info(MSG_HELP_6, player);
    }

    private void executeListAll(Player player, String worldName) {
        if(worldName == null) {
            PlayerMessage.info("Listing all smokes:", player);
        } else {
            PlayerMessage.info("Listing all smokes in world %s:".formatted(worldName), player);
        }
        smokeLocationService.getAll(worldName).forEach(smokeLocation ->
                PlayerMessage.info("Smoke (%s) - at [%s, %s, %s]. Spawn-Frequency: %s".formatted(smokeLocation.getId(), smokeLocation.getX(), smokeLocation.getY(), smokeLocation.getZ(), smokeLocation.getFrequency()), player)
        );
    }

    private void executeRemoveSmoke(Player player, Integer id) {
        smokeLocationService.deleteSmoke(id);
        PlayerMessage.success("Deleted smoke: %s".formatted(id), player);
    }

    private void executeRemoveAll(Player player, String worldName) {
        if(worldName == null) {
            PlayerMessage.success("Deleted all smokes", player);
        } else {
            PlayerMessage.success("Deleted all smokes in world %s".formatted(worldName), player);
        }
        smokeLocationService.deleteAll(worldName);
    }
}