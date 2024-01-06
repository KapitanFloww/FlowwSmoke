package de.flowwindustries.flowwsmoke.service.impl;

import de.flowwindustries.flowwsmoke.FlowwSmoke;
import de.flowwindustries.flowwsmoke.domain.SmokeLocation;
import de.flowwindustries.flowwsmoke.service.SmokeTaskService;
import de.flowwindustries.flowwsmoke.tasks.SpawnSmokeTask;
import lombok.extern.java.Log;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Log
public class SmokeTaskServiceImpl implements SmokeTaskService {

    private final double spawnDeviation;
    private final BukkitScheduler scheduler;
    private final Map<SmokeLocation, Integer> scheduledTaskLocations = new ConcurrentHashMap<>();

    public SmokeTaskServiceImpl(BukkitScheduler scheduler, double spawnDeviation) {
        this.scheduler = Objects.requireNonNull(scheduler);
        this.spawnDeviation = spawnDeviation;
    }

    @Override
    public void scheduleSmokeTask(final SmokeLocation smokeLocation) {
        final var task = new SpawnSmokeTask(spawnDeviation, smokeLocation);
        final int taskId = scheduler.scheduleSyncRepeatingTask(
                FlowwSmoke.getInstance(),
                task,
                0L,
                smokeLocation.getFrequency()
        );
        scheduledTaskLocations.put(smokeLocation, taskId);
        log.config("Scheduled spawn-smoke-task: %s. Frequency: %s".formatted(taskId, smokeLocation.getFrequency()));
    }

    @Override
    public void cancelTask(final SmokeLocation smokeLocation) {
        final Integer taskId = scheduledTaskLocations.get(smokeLocation);
        if (taskId != null) {
            cancelTask(taskId);
        }
    }

    @Override
    public void cancelAll() {
        scheduledTaskLocations.values().forEach(this::cancelTask);
    }

    private void cancelTask(final int taskId) {
        scheduler.cancelTask(taskId);
        log.config("Cancelled task with id: %s".formatted(taskId));
    }
}
