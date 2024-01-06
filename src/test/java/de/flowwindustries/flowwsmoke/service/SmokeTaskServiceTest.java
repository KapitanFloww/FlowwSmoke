package de.flowwindustries.flowwsmoke.service;

import de.flowwindustries.flowwsmoke.domain.SmokeLocation;
import de.flowwindustries.flowwsmoke.service.impl.SmokeTaskServiceImpl;
import org.assertj.core.api.Assertions;
import org.bukkit.scheduler.BukkitScheduler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SmokeTaskServiceTest {

    private final BukkitScheduler scheduler = new TestBukkitScheduler();
    private final SmokeTaskService smokeTaskService = new SmokeTaskServiceImpl(scheduler, 0.0);

    @BeforeEach
    @AfterEach
    void setUp() {
        TestBukkitScheduler.TASK_COUNTER = 0; // Reset the counter
    }

    @Test
    void verifyScheduleTask() {
        Assertions.assertThat(TestBukkitScheduler.TASK_COUNTER).isEqualTo(0);

        smokeTaskService.scheduleSmokeTask(createSmokeLocation(1.0));
        smokeTaskService.scheduleSmokeTask(createSmokeLocation(2.0));

        // verify 3 tasks scheduled
        Assertions.assertThat(TestBukkitScheduler.TASK_COUNTER).isEqualTo(2);
    }

    @Test
    void verifyCancelTask() {
        final var location1 = createSmokeLocation(1.0);
        final var location2 = createSmokeLocation(2.0);
        smokeTaskService.scheduleSmokeTask(location1);
        smokeTaskService.scheduleSmokeTask(location2);
        // verify 3 tasks scheduled
        Assertions.assertThat(TestBukkitScheduler.TASK_COUNTER).isEqualTo(2);

        smokeTaskService.cancelTask(location2);

        Assertions.assertThat(TestBukkitScheduler.TASK_COUNTER).isEqualTo(1);
    }

    @Test
    void verifyCancelAll() {
        smokeTaskService.scheduleSmokeTask(createSmokeLocation(1.0));
        smokeTaskService.scheduleSmokeTask(createSmokeLocation(2.0));
        // verify 3 tasks scheduled
        Assertions.assertThat(TestBukkitScheduler.TASK_COUNTER).isEqualTo(2);

        smokeTaskService.cancelAll();

        Assertions.assertThat(TestBukkitScheduler.TASK_COUNTER).isEqualTo(0);
    }

    private static SmokeLocation createSmokeLocation(double x) {
        return new SmokeLocation()
                .withX(x)
                .withY(2.0)
                .withZ(3.0)
                .withWorldName("world")
                .withId(1)
                .withFrequency(1);
    }
}