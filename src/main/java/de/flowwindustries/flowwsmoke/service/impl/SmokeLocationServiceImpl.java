package de.flowwindustries.flowwsmoke.service.impl;

import com.google.common.base.Strings;
import de.flowwindustries.flowwsmoke.domain.SmokeLocation;
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
    private final SmokeTaskServiceImpl smokeTaskService;

    public SmokeLocationServiceImpl(SmokeLocationIOService ioService, SmokeTaskServiceImpl smokeTaskService) {
        this.smokeTaskService = Objects.requireNonNull(smokeTaskService);
        this.ioService = Objects.requireNonNull(ioService);
        this.smokeLocations = ioService.loadLocations();
        this.counter = setCounter(this.smokeLocations);
    }

    @Override
    public int addSmoke(SmokeLocation smokeLocation) {
        // Add location
        int currentId = counter++;
        smokeLocation.setId(currentId);
        smokeLocations.add(smokeLocation);

        // Schedule new task for created location
        smokeTaskService.scheduleSmokeTask(smokeLocation);

        log.config("Added smoke location %s at [%s , %s, %s]".formatted(smokeLocation.getId(), smokeLocation.getX(), smokeLocation.getY(), smokeLocation.getZ()));

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
        if (Strings.isNullOrEmpty(worldName)) {
            return getAll();
        }
        return smokeLocations.stream()
                .filter(smokeLocation -> smokeLocation.getWorldName().equals(worldName))
                .toList();
    }

    @Override
    public List<SmokeLocation> getAll() {
        return smokeLocations;
    }

    @Override
    public void deleteSmoke(int id) {
        var location = getSmoke(id);
        if(location != null) {
            // Remove scheduled task
            smokeTaskService.cancelTask(location);

            // Remove location
            smokeLocations.remove(location);
            log.config("Delete smoke location %s".formatted(id));

            persistLocations();
        }
    }

    @Override
    public void deleteAll(String worldName) {
        if (Strings.isNullOrEmpty(worldName)) {
            deleteAll();
            return;
        }

        List<SmokeLocation> temp = getAll(worldName);
        // Remove scheduled tasks
        temp.forEach(smokeTaskService::cancelTask);

        // Remove locations
        smokeLocations.removeAll(temp);
        log.config("Deleted all smoke locations in world %s".formatted(worldName));
        persistLocations();
    }

    @Override
    public void deleteAll() {
        // Remove all scheduled tasks
        smokeTaskService.cancelAll();

        // Remove locations
        smokeLocations.clear();
        log.config("Deleting all smoke locations");
        persistLocations();
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
