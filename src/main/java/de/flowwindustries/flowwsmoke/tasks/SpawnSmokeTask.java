package de.flowwindustries.flowwsmoke.tasks;

import de.flowwindustries.flowwsmoke.domain.SmokeLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Objects;
import java.util.Optional;

public class SpawnSmokeTask implements Runnable {

    private final SmokeLocation smokeLocation;

    public SpawnSmokeTask(SmokeLocation smokeLocation) {
        this.smokeLocation = Objects.requireNonNull(smokeLocation);
    }

    @Override
    public void run() {
        final var world = Optional.ofNullable(Bukkit.getWorld(smokeLocation.getWorldName()))
                .orElseThrow(() -> new IllegalStateException("World with name \"%s\" not found!".formatted(smokeLocation.getWorldName())));
        final var location = new Location(world, smokeLocation.getX(), smokeLocation.getY(), smokeLocation.getZ());
        final var pattern = new SmokeSpawnPattern();
        pattern.execute(world, location);
    }
}
