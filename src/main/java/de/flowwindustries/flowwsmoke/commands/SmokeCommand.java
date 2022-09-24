package de.flowwindustries.flowwsmoke.commands;

import de.flowwindustries.flowwsmoke.FlowwSmoke;
import de.flowwindustries.flowwsmoke.utils.messages.PlayerMessage;
import lombok.extern.java.Log;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Smoke Command to create small smokers.
 * /smoke - create new smoke
 * /smoke save - save all smokes
 * /smoke load - load all saved smokes
 * /smoke list - list all smokes
 * /smoke remove (id/all) - remove smoke with id or all smokes
 */
@Log
public class SmokeCommand extends AbstractCommand {

    public SmokeCommand(String permission) {
        super(permission);
    }

    @Override
    protected boolean playerCommand(Player player, String[] args) {
        switch (args.length) {
            case 0 -> {
                executeSmokeCreation(player);
            }
            case 1 -> {
                switch (args[0].toLowerCase(Locale.getDefault())) {
                    case "help" -> executeHelp(player);
                    case "list" -> executeListAll(player);
                    case "save" -> {
                        SmokeCommand.persistLocations();
                        PlayerMessage.success("Saved locations", player);
                    }
                    case "load" -> {
                        SmokeCommand.initializeLocations();
                        PlayerMessage.success("Initialized locations", player);
                    }
                }
            }
            case 2 -> {
                if(!args[0].equalsIgnoreCase("remove")) {
                    throw new IllegalArgumentException(INVALID_ARGUMENTS + ": %s".formatted(args[0]));
                }
                if(args[1].equalsIgnoreCase("all")) {
                    executeRemoveAll(player);
                    return false;
                }
                int id = Integer.parseInt(args[1]);
                executeRemoveSmoke(player, id);
            }
            default -> throw new IllegalArgumentException(INVALID_ARGUMENTS);
        }
        return false;
    }

    private void executeHelp(Player player) {
        PlayerMessage.info("-- Smoke Help --", player);
        PlayerMessage.info("/smoke - create new smoke", player);
        PlayerMessage.info("/smoke save - save all smokes", player);
        PlayerMessage.info("/smoke load - load all saved smokes", player);
        PlayerMessage.info("/smoke list - list all smokes", player);
        PlayerMessage.info("/smoke remove (id/all) - remove smoke with id or all smokes", player);
    }

    @Override
    protected boolean consoleCommand(String[] args) {
        Bukkit.getConsoleSender().sendMessage("Can only be used in-game");
        return false;
    }

    private static void executeListAll(Player player) {
        smokeLocations.forEach((taskId, location) -> PlayerMessage.info("Id: %s, Location: %s".formatted(taskId, location), player));
    }

    private static void executeRemoveAll(Player player) {
        smokeLocations.clear();
        PlayerMessage.info("Cleared all locations", player);
    }

    private static void executeRemoveSmoke(Player player, Integer id) {
        if(smokeLocations.containsKey(id)) {
            PlayerMessage.info("Removed location: %s".formatted(id), player);
            smokeLocations.remove(id);
            return;
        }
        PlayerMessage.warn("Could not find task: %s".formatted(id), player);
    }

    private static void executeSmokeCreation(Player player) {
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
        int id = counter++;
        smokeLocations.put(id, targetLocation);
        PlayerMessage.success("Placed smoke at %s, %s, %s (taskId: %s)".formatted(targetLocation.getBlock().getX(), targetLocation.getBlock().getY(), targetLocation.getBlock().getZ(), id), player);
    }
}