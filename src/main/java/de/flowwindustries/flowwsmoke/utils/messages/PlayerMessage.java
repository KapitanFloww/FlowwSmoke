package de.flowwindustries.flowwsmoke.utils.messages;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;

import static de.flowwindustries.flowwsmoke.FlowwSmoke.PREFIX;

/**
 * Utility class to simplify sending of colored messages to a player.
 */
public class PlayerMessage {

    /**
     * Send an {@code INFO} ({@link ChatColor#YELLOW}) message to a player.
     * @param message the message to send
     * @param players the players to send the message to
     */
    public static void info(String message, Player... players) {
        sendMessageIntern(ChatColor.YELLOW, message, players);
    }

    /**
     * Send an {@code SUCCESS} ({@link ChatColor#GREEN}) message to a player.
     * @param message the message to send
     * @param players the players to send the message to
     */
    public static void success(String message, Player... players) {
        sendMessageIntern(ChatColor.GREEN, message, players);
    }

    /**
     * Send an {@code WARN} ({@link ChatColor#RED}) message to a player.
     * @param message the message to send
     * @param players the players to send the message to
     */
    public static void warn(String message, Player... players) {
        sendMessageIntern(ChatColor.RED, message, players);
    }

    /**
     * Send an {@code ERROR} ({@link ChatColor#DARK_RED}) message to a player.
     * @param message the message to send
     * @param players the players to send the message to
     */
    public static void error(String message, Player... players) {
        sendMessageIntern(ChatColor.DARK_RED, message, players);
    }

    private static void sendMessageIntern(ChatColor chatColor, String message, Player... players) {
        Arrays.stream(players).forEach(player -> player.sendMessage(String.format("%s%s%s", PREFIX, chatColor, message)));
    }
}
