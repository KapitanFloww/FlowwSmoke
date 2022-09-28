package de.flowwindustries.flowwsmoke.service.impl;

import de.flowwindustries.flowwsmoke.domain.SmokeLocation;
import de.flowwindustries.flowwsmoke.domain.SmokeLocationDTO;
import de.flowwindustries.flowwsmoke.service.SmokeLocationIOService;
import de.flowwindustries.flowwsmoke.service.SmokeLocationService;
import lombok.extern.java.Log;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Service implementation of {@link SmokeLocationService}.
 */
@Log
public class SmokeLocationServiceImpl implements SmokeLocationService {

    private int counter;
    private final List<SmokeLocation> smokeLocations;

    // Dependencies
    private final SmokeLocationIOService ioService;

    public SmokeLocationServiceImpl(SmokeLocationIOService ioService) {
        this.ioService = Objects.requireNonNull(ioService);
        this.smokeLocations = ioService.loadLocations();
        this.counter = setCounter(this.smokeLocations);
    }

    @Override
    public int addSmoke(SmokeLocationDTO smokeLocationDTO) {
        int currentId = counter++;
        smokeLocations.add(new SmokeLocation()
                .withWorldName(smokeLocationDTO.getWorldName())
                .withX(smokeLocationDTO.getX())
                .withY(smokeLocationDTO.getY())
                .withZ(smokeLocationDTO.getZ())
                .withId(currentId));
        log.config("Added smoke location %s at [%s , %s, %s]".formatted(currentId, smokeLocationDTO.getX(), smokeLocationDTO.getY(), smokeLocationDTO.getZ()));

        persistLocations();

        return currentId;
    }

    @Override
    public SmokeLocation getSmoke(int id) throws IllegalStateException {
        var results = smokeLocations.stream().filter(smokeLocation -> smokeLocation.getId().equals(id)).toList();
        if(results.size() == 0) {
            return null;
        }
        if(results.size() != 1) {
            throw new IllegalStateException("Ambiguous smoke locations for id %s found".formatted(id));
        }
        return results.get(0);
    }

    @Override
    public List<SmokeLocation> getAll(String worldName) {
        if(worldName == null) {
            return smokeLocations;
        }
        return listForWorld(worldName);
    }

    @Override
    public void deleteSmoke(int id) {
        var location = getSmoke(id);
        if(location != null) {
            smokeLocations.remove(location);
            log.config("Delete smoke location %s".formatted(id));

            // Persist locations
            persistLocations();
        }
    }

    @Override
    public void deleteAll(String worldName) {
        if(worldName == null) {
            smokeLocations.clear();
            log.config("Deleting all smoke locations");
            return;
        }
        List<SmokeLocation> temp = listForWorld(worldName);
        smokeLocations.removeAll(temp);
        log.config( "Deleted all smoke locations in world %s".formatted(worldName));

        persistLocations();
    }

    private List<SmokeLocation> listForWorld(String worldName) {
        return smokeLocations.stream()
                .filter(smokeLocation -> smokeLocation.getWorldName().equals(worldName))
                .toList();
    }

    private static int setCounter(List<SmokeLocation> locations) {
        AtomicInteger temp = new AtomicInteger(0);
        locations.forEach(smokeLocation -> {
            if(smokeLocation.getId() > temp.get()) {
                temp.set(smokeLocation.getId());
            }
        });
        return temp.get() + 1;
    }

    private void persistLocations() {
        // Persist locations
        ioService.persistLocations(smokeLocations);
    }
}
