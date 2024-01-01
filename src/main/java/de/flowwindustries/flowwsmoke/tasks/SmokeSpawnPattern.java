package de.flowwindustries.flowwsmoke.tasks;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

import java.util.Random;

/**
 * Pseudo-Random spawn pattern for smoke particles.
 */
public class SmokeSpawnPattern {

    private static final Random RAND = new Random();

    public void execute(final World world, final Location location) {
        if(RAND.nextInt(50) % 3 == 0) {
            return;
        }
        if(!(RAND.nextInt(50) % 3 == 0)) {
            world.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, location, 0, 1, 10.0d, 0.0d, 0.01d);
            return;
        }
        if(!(RAND.nextInt(50) % 2 == 0)) {
            world.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, location, 0, 0.0d, 10.0d, 0.5d, 0.01d);
            return;
        }
        if(!(RAND.nextInt(50) % 4 == 0)) {
            world.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, location, 0, -0.4d, 10.0d, -1, 0.01d);
        }
    }
}
