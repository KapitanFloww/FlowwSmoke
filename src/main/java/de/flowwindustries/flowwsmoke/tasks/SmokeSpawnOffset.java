package de.flowwindustries.flowwsmoke.tasks;

import de.flowwindustries.flowwsmoke.domain.SmokeLocation;

public record SmokeSpawnOffset(double x, double y, double z) {

    public static SmokeSpawnOffset ofLocation(SmokeLocation smokeLocation) {
        return new SmokeSpawnOffset(smokeLocation.getOffsetX(), smokeLocation.getOffsetY(), smokeLocation.getOffsetZ());
    }

}
