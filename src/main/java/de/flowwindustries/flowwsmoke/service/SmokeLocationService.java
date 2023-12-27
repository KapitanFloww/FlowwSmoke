package de.flowwindustries.flowwsmoke.service;

import de.flowwindustries.flowwsmoke.domain.SmokeLocation;

import java.util.List;

/**
 * Service class to create, get and delete {@link SmokeLocation}s.
 */
public interface SmokeLocationService {

    /**
     * Create and add a new smoke location.
     * @param smokeLocation - the smoke location to add
     * @return the identifier of the newly created location
     */
    int addSmoke(SmokeLocation smokeLocation);

    /**
     * Get a specific smoke location by its identifier.
     * @param id - the identifier of the smoke location to get
     * @return the requested {@link SmokeLocation} or {@code null} if not found
     * @throws IllegalStateException if more than one {@link SmokeLocation} with the given id is found
     */
    SmokeLocation getSmoke(int id) throws IllegalStateException;

    /**
     * Get all smoke locations for the specified world.
     * @param worldName - name of the world to get locations for
     * @return a {@link List} containing all {@link SmokeLocation}s of the given world
     */
    List<SmokeLocation> getAll(String worldName);

    /**
     * Get all smoke locations.
     * @return a {@link List} containting all {@link SmokeLocation}s
     */
    List<SmokeLocation> getAll();

    /**
     * Delete a specific smoke location by its identifier.
     * @param id - the identifier of the smoke location to delete
     */
    void deleteSmoke(int id);

    /**
     * Delete all smoke locations of the given world.
     * @param worldName - if {@code null} delete all registered locations, else only delete locations of the given world
     */
    void deleteAll(String worldName);

    /**
     * Delete all smoke locations.
     */
    void deleteAll();
}
