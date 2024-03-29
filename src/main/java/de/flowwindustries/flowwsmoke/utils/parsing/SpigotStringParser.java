package de.flowwindustries.flowwsmoke.utils.parsing;

import de.flowwindustries.flowwsmoke.lang.LanguageLoader;
import lombok.extern.java.Log;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Optional;

import static de.flowwindustries.flowwsmoke.FlowwSmoke.pluginPrefix;

/**
 * Utility class that provides methods to (safely) parse Strings to either an
 * <ul>
 *     <li>{@link Player},</li>
 *     <li>{@link Integer},</li>
 *     <li>{@link Double},</li>
 *     <li>{@link Float}</li>
 * </ul>
 * If the {@link String} cannot be parsed to the requested object a corresponding {@link IllegalArgumentException} will be thrown.
 */
@Log
public class SpigotStringParser {

    private static final String PARSE_ERROR = LanguageLoader.getMessage("messages.errors.argument-not-parseable");
    private static final String PARAMETER_NULL = LanguageLoader.getMessage("messages.errors.input-argument-null");

    /**
     * Parse the given {@link String} to an <strong>online</strong> {@link Player}.
     * @param playerName the players name
     * @return the parsed {@link Player}
     * @throws IllegalArgumentException if the {@link String} cannot be parsed to an {@link Player}. This could mean, that the either the player is offline or the playerName is invalid.
     */
    public static Player parsePlayerSafe(String playerName) throws IllegalArgumentException {
        if(playerName == null) {
            throw new IllegalArgumentException(PARAMETER_NULL);
        }

        log.config(pluginPrefix + "Trying to parse player: " + playerName);
        Optional<Player> optionalPlayer = Optional.ofNullable(Bukkit.getPlayer(playerName));
        return optionalPlayer.orElseThrow(() -> new IllegalArgumentException(PARSE_ERROR.replace("{argument}", playerName)));
    }

    /**
     * Parse the given {@link String} to an {@link World}.
     * @param worldName the world's name
     * @return the parsed {@link World}
     * @throws IllegalArgumentException if unable to parse the world
     */
    public static World parseWorldSafe(String worldName) throws IllegalArgumentException {
        if(worldName == null) {
            throw new IllegalArgumentException(PARAMETER_NULL);
        }

        log.config(pluginPrefix + "Trying to parse world: " + worldName);
        Optional<World> optionalWorld = Optional.ofNullable(Bukkit.getWorld(worldName));
        return optionalWorld.orElseThrow(() -> new IllegalArgumentException(PARSE_ERROR.replace("{argument}", worldName)));
    }

    /**
     * Parse the given {@link String} to an {@link Integer}.
     * @param integerString the {@link String} to be parsed
     * @return the parsed {@link Integer}
     * @throws IllegalArgumentException if the {@link String} cannot be parsed to an {@link Integer}
     */
    public static int parseIntSafe(String integerString) throws IllegalArgumentException {
        if(integerString == null) {
            throw new IllegalArgumentException(PARAMETER_NULL);
        }

        log.config(pluginPrefix + "Trying to parse integer: " + integerString);
        try {
            return Integer.parseInt(integerString);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(PARSE_ERROR.replace("{argument}", integerString));
        }
    }

    /**
     * Parse the given {@link String} to an {@link Double}.
     * @param doubleString the {@link String} to be parsed
     * @return the parsed {@link Double}
     * @throws IllegalArgumentException if the {@link String} cannot be parsed to an {@link Double}
     */
    public static double parseDoubleSafe(String doubleString) throws IllegalArgumentException {
        if(doubleString == null) {
            throw new IllegalArgumentException(PARAMETER_NULL);
        }

        log.config(pluginPrefix + "Trying to parse double: " + doubleString);
        try {
            return Double.parseDouble(doubleString);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(PARSE_ERROR.replace("{argument}", doubleString));
        }
    }

    /**
     * Parse the given {@link String} to an {@link Float}.
     * @param floatString the {@link String} to be parsed
     * @return the parsed {@link Float}
     * @throws IllegalArgumentException if the {@link String} cannot be parsed to an {@link Float}
     */
    public static float parseFloatSafe(String floatString) throws IllegalArgumentException {
        if(floatString == null) {
            throw new IllegalArgumentException(PARAMETER_NULL);
        }

        log.config(pluginPrefix + "Trying to parse float: " + floatString);
        try {
            return Float.parseFloat(floatString);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(PARSE_ERROR.replace("{argument}", floatString));
        }
    }
}
