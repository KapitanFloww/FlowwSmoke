package de.flowwindustries.flowwsmoke.tasks;

import de.flowwindustries.flowwsmoke.domain.SmokeLocation;
import de.flowwindustries.flowwsmoke.utils.parsing.SpigotStringParser;
import org.bukkit.Location;

import java.util.Objects;

public class SpawnSmokeTask implements Runnable {

    private final SmokeLocation smokeLocation;

    public SpawnSmokeTask(SmokeLocation smokeLocation) {
        this.smokeLocation = Objects.requireNonNull(smokeLocation);
    }

    @Override
    public void run() {
        final var world = SpigotStringParser.parseWorldSafe(smokeLocation.getWorldName());
        final var location = new Location(world, smokeLocation.getX(), smokeLocation.getY(), smokeLocation.getZ());
        final var pattern = new SmokeSpawnPattern();
        pattern.execute(world, location);
    }
}
