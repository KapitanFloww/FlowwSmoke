package de.flowwindustries.flowwsmoke.service;

import de.flowwindustries.flowwsmoke.domain.SmokeLocation;
import de.flowwindustries.flowwsmoke.domain.SmokeLocationDTO;

import java.util.List;

/**
 * Service class to create, get and delete {@link SmokeLocation}s.
 */
public interface SmokeLocationService {

    /**
     * Create and add a new smoke location.
     * @param smokeLocationDTO - data transfer object for smoke locations
     * @return the identifier of the newly created location
     */
    int addSmoke(SmokeLocationDTO smokeLocationDTO);

    /**
     * Get a specific smoke location by its identifier.
     * @param id - the identifier of the smoke location to get
     * @return the requested {@link SmokeLocation} or {@code null} if not found
     * @throws IllegalStateException if more than one {@link SmokeLocation} with the given id is found
     */
    SmokeLocation getSmoke(int id) throws IllegalStateException;

    /**
     * Try to get the {@link SmokeLocation} at this specific target location ({@link SmokeLocationDTO}.
     * If more than {@link SmokeLocation} at the DTO-location is found, then the first found will be removed.
     * @param dto - the DTO-location to check
     * @return a {@link List} with all matching {@link SmokeLocation}s, never {@code null}
     * @throws IllegalArgumentException if the DTO-location cannot be parsed to an existing {@link SmokeLocation}
     */
    List<SmokeLocation> getSmokeAtDtoSafe(SmokeLocationDTO dto) throws IllegalArgumentException, IllegalStateException;

    /**
     * Get all smoke locations.
     * @param worldName - if {@code null} return all registered locations, else return locations of the given world
     * @return a {@link List} containing all {@link SmokeLocation}s
     */
    List<SmokeLocation> getAll(String worldName);

    /**
     * Delete a specific smoke location by its identifier.
     * @param id - the identifier of the smoke location to delete
     */
    void deleteSmoke(int id);

    /**
     * Delete all smoke locations.
     * @param worldName - if {@code null} delete all registered locations, else only delete locations of the given world
     */
    void deleteAll(String worldName);
}
