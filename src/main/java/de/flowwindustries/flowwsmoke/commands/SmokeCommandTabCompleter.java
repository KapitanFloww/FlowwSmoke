package de.flowwindustries.flowwsmoke.commands;

import de.flowwindustries.flowwsmoke.service.SmokeLocationService;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class SmokeCommandTabCompleter implements TabCompleter {

    private final SmokeLocationService smokeLocationService;

    public SmokeCommandTabCompleter(SmokeLocationService smokeLocationService) {
        this.smokeLocationService = Objects.requireNonNull(smokeLocationService);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return Collections.emptyList();
        }
        final var locations = smokeLocationService.getAll();
        final var ids = locations.stream().map(smokeLocation -> smokeLocation.getId().toString());
        final var worlds = Bukkit.getWorlds().stream().map(World::getName).toList();
        return switch (args.length) {
            case 1 -> List.of("add", "remove", "list", "help");
            case 2 -> switch (args[0]) {
                case "remove" -> Stream.concat(ids, Stream.of("all")).toList();
                case "list" -> worlds;
                default -> Collections.emptyList();
            };
            default -> Collections.emptyList();
        };
    }
}
