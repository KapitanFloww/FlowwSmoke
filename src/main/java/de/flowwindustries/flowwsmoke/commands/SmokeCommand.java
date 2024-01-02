package de.flowwindustries.flowwsmoke.commands;

import de.flowwindustries.flowwsmoke.domain.SmokeLocation;
import de.flowwindustries.flowwsmoke.lang.LanguageLoader;
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
 * /smoke add (frequency) [offsetX] [offsetY] [offsetZ] - create a new custom smoke spawn
 * /smoke list [world] - list all smokes
 * /smoke remove (id) - remove smoke with id or all smokes
 * /smoke remove all [world] - remove all smokes
 */
@Log
public class SmokeCommand implements CommandExecutor {

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
                    throw new IllegalArgumentException(LanguageLoader.getMessage("messages.errors.insufficient-permission"));
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
                    default -> throw new IllegalArgumentException(LanguageLoader.getMessage("messages.errors.unknown-arguments").replace("{argument}", args[0]));
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
                                throw new IllegalArgumentException(LanguageLoader.getMessage("messages.errors.unknown-arguments").replace("{argument}", args[1]));
                            }
                            executeRemoveAll(player, null); // smoke remove all
                        }
                    }
                    default -> throw new IllegalArgumentException(LanguageLoader.getMessage("messages.errors.unknown-arguments").replace("{argument}", args[0]));
                }
            }
            case 3 -> {
                if(!(args[0].equalsIgnoreCase("remove") && args[1].equalsIgnoreCase("all"))) {
                    throw new IllegalArgumentException(LanguageLoader.getMessage("messages.errors.unknown-arguments").replace("{argument}", args[0] + ", " +args[1]));
                }
                executeRemoveAll(player, args[2]); // smoke remove all <world>
            }
            case 5 -> {
                // smoke add (frequency) (offsetX) (offsetY) (offsetZ)
                if (!args[0].equalsIgnoreCase("add")) {
                    throw new IllegalArgumentException(LanguageLoader.getMessage("messages.errors.unknown-arguments").replace("{argument}", args[0]));
                }
                final int spawnFrequency = SpigotStringParser.parseIntSafe(args[1]);
                final double offsetX = SpigotStringParser.parseDoubleSafe(args[2]);
                final double offsetY = SpigotStringParser.parseDoubleSafe(args[3]);
                final double offsetZ = SpigotStringParser.parseDoubleSafe(args[4]);
                executeSmokeCreation(player, spawnFrequency, offsetX, offsetY, offsetZ);
            }
            default -> throw new IllegalArgumentException(LanguageLoader.getMessage("messages.errors.unknown-arguments-length").replace("{arguments_length}", String.valueOf(args.length)));
        }
        return true; //remove all <world>
    }

    private boolean consoleCommand(String[] args) {
        log.info(LanguageLoader.getMessage("messages.errors.console-not-supported"));
        return false;
    }

    private void executeSmokeCreation(Player player) {
        executeSmokeCreation(player, fallbackFrequency);
    }

    private void executeSmokeCreation(Player player, int frequency) {
        executeSmokeCreation(player, frequency, 0.0, 0.0, 0.0);
    }

    private void executeSmokeCreation(Player player, int frequency, double offsetX, double offsetY, double offsetZ) {
        if (frequency < 1) {
            throw new IllegalArgumentException(LanguageLoader.getMessage("messages.errors.frequency-less-equal-zero"));
        }
        Block targetBlock = player.getTargetBlockExact(5);
        if(targetBlock == null) {
            throw new IllegalArgumentException(LanguageLoader.getMessage("messages.errors.not-looking-at-block"));
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
                .withZ(z)
                .withOffsetX(offsetX)
                .withOffsetY(offsetY)
                .withOffsetZ(offsetZ);
        int id = smokeLocationService.addSmoke(location);
        PlayerMessage.success(LanguageLoader.getMessage("messages.smoke.placed-success")
                        .replace("{id}", String.valueOf(id))
                        .replace("{x}", String.valueOf(location.getX()))
                        .replace("{y}", String.valueOf(location.getY()))
                        .replace("{z}", String.valueOf(location.getZ()))
                        .replace("{frequency}", String.valueOf(location.getFrequency()))
                        .replace("{customOffset}", String.valueOf(hasCustomOffset(location))),
                player);
    }

    private void executeHelp(Player player) {
        PlayerMessage.info(LanguageLoader.getMessage("messages.smoke.help"), player);
    }

    private void executeListAll(Player player, String worldName) {
        if(worldName == null) {
            PlayerMessage.info(LanguageLoader.getMessage("messages.smoke.list-all"), player);
        } else {
            PlayerMessage.info(LanguageLoader.getMessage("messages.smoke.list-all-world").replace("{world}", worldName), player);
        }
        smokeLocationService.getAll(worldName).forEach(location ->
                PlayerMessage.success(LanguageLoader.getMessage("messages.smoke.list-info-item")
                                .replace("{id}", String.valueOf(location.getId()))
                                .replace("{x}", String.valueOf(location.getX()))
                                .replace("{y}", String.valueOf(location.getY()))
                                .replace("{z}", String.valueOf(location.getZ()))
                                .replace("{frequency}", String.valueOf(location.getFrequency()))
                                .replace("{customOffset}", String.valueOf(hasCustomOffset(location))),
                        player)
        );
    }

    private void executeRemoveSmoke(Player player, Integer id) {
        smokeLocationService.deleteSmoke(id);
        PlayerMessage.success(LanguageLoader.getMessage("messages.smoke.delete-success").replace("{id}", String.valueOf(id)), player);
    }

    private void executeRemoveAll(Player player, String worldName) {
        if(worldName == null) {
            PlayerMessage.success(LanguageLoader.getMessage("messages.smoke.delete-all"), player);
        } else {
            PlayerMessage.success(LanguageLoader.getMessage("messages.smoke.delete-all-world").replace("{world}", worldName), player);
        }
        smokeLocationService.deleteAll(worldName);
    }

    private static boolean hasCustomOffset(SmokeLocation smokeLocation) {
        return smokeLocation.getOffsetX() != 0.0 || smokeLocation.getOffsetY() != 0.0 || smokeLocation.getOffsetZ() != 0.0;
    }
}