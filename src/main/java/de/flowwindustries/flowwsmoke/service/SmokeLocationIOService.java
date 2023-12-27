package de.flowwindustries.flowwsmoke.service;

import de.flowwindustries.flowwsmoke.domain.SmokeLocation;

import java.util.List;

/**
 * Service class to persist and load smoke locations.
 */
public interface SmokeLocationIOService {

    /**
     * Persist all registered smoke locations.
     * @param locations - all locations to persist
     */
    void persistLocations(List<SmokeLocation> locations);

    /**
     * Load all smoke locations.
     * @return a {@link List} of persisted {@link SmokeLocation}s or a new and empty {@link java.util.ArrayList} if no import file is found
     */
    List<SmokeLocation> loadLocations();
}
