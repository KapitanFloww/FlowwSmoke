package de.flowwindustries.flowwsmoke.tasks;

import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

import java.util.Random;

/**
 * Pseudo-Random spawn pattern for smoke particles.
 */
public class SmokeSpawnPattern {

    private static final double DATA = 0.01; // Without this, particles will flicker randomly
    private static final Random RAND = new Random();

    private final double deviation;

    public SmokeSpawnPattern(double deviation) {
        Preconditions.checkArgument(deviation >= 0);
        this.deviation = deviation;
    }

    public void execute(final World world, final Location location, final SmokeSpawnOffset offset) {
        double offsetX = offset.x();
        double offsetY = offset.y() + 10.0; // To make it fly up always
        double offsetZ = offset.z();

        // Add deviation is allowed create and offset
        if (deviation != 0) {
            offsetX += RAND.nextDouble(-deviation, deviation);
            offsetY += RAND.nextDouble(-deviation, deviation);
            offsetZ += RAND.nextDouble(-deviation, deviation);
        }

        final int numerator = RAND.nextInt(1, 50);
        final int denominator = RAND.nextInt(1, 10);
        if(numerator % denominator == 0) {
            return;
        }
        world.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, location, 0, offsetX, offsetY, offsetZ, DATA);
    }
}
