package de.flowwindustries.flowwsmoke.service;

import de.flowwindustries.flowwsmoke.domain.SmokeLocation;

/**
 * Service class to schedule and cancel smoke-spawn tasks.
 */
public interface SmokeTaskService {

    /**
     * Schedule a new spawn task for the given location.
     * @param smokeLocation
     */
    void scheduleSmokeTask(SmokeLocation smokeLocation);

    /**
     * Cancel the spawn task for the given location.
     * @param smokeLocation
     */
    void cancelTask(SmokeLocation smokeLocation);

    /**
     * Cancel all registered spawn tasks.
     */
    void cancelAll();
}
