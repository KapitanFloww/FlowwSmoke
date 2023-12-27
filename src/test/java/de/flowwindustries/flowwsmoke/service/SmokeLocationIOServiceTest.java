package de.flowwindustries.flowwsmoke.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.flowwindustries.flowwsmoke.domain.SmokeLocation;
import de.flowwindustries.flowwsmoke.service.impl.SmokeLocationIOJsonServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static de.flowwindustries.flowwsmoke.service.SmokeLocationServiceImplTest.DUMMY_FREQUENCY;
import static de.flowwindustries.flowwsmoke.service.SmokeLocationServiceImplTest.DUMMY_WORLD;
import static de.flowwindustries.flowwsmoke.service.SmokeLocationServiceImplTest.DUMMY_X;
import static de.flowwindustries.flowwsmoke.service.SmokeLocationServiceImplTest.DUMMY_Y;
import static de.flowwindustries.flowwsmoke.service.SmokeLocationServiceImplTest.DUMMY_Z;
import static de.flowwindustries.flowwsmoke.service.SmokeLocationServiceImplTest.assertSmokeLocation;
import static org.assertj.core.api.Assertions.assertThat;

public class SmokeLocationIOServiceTest {

    private static final Integer DUMMY_ID = 1;
    private static final String TEST_FILE_PATH = "test.json";
    private static final File TEST_FILE = new File(TEST_FILE_PATH);

    // Unit-under-test
    private final SmokeLocationIOService smokeLocationIOService;

    public SmokeLocationIOServiceTest() {
        this.smokeLocationIOService = new SmokeLocationIOJsonServiceImpl(TEST_FILE, new ObjectMapper(), Runnable::run); // simply run the runnable (no async for tests)
    }

    @BeforeEach
    void setUp() {
        TEST_FILE.delete();
    }

    @Test
    void verifyPersistLocations() {
        // GIVEN
        List<SmokeLocation> locations = getLocationsList();
        // WHEN
        smokeLocationIOService.persistLocations(locations);
        // THEN
        assertThat(TEST_FILE).exists();
        assertThat(TEST_FILE).isFile();
    }

    @Test
    void verifyPersistLocationsEmpty() {
        // GIVEN
        List<SmokeLocation> locations = getEmptyLocationsList();
        // WHEN
        smokeLocationIOService.persistLocations(locations);
        // THEN
        assertThat(TEST_FILE).exists();
        assertThat(TEST_FILE).isFile();
    }

    @Test
    void verifyLoadLocations() {
        // GIVEN
        List<SmokeLocation> locations = getLocationsList();
        smokeLocationIOService.persistLocations(locations);
        // WHEN
        List<SmokeLocation> result = smokeLocationIOService.loadLocations();
        // THEN
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertSmokeLocation(result.get(0), DUMMY_ID, DUMMY_WORLD);
    }

    @Test
    void verifyLoadLocationsEmpty() {
        // GIVEN
        List<SmokeLocation> locations = getEmptyLocationsList();
        smokeLocationIOService.persistLocations(locations);
        // WHEN
        List<SmokeLocation> result = smokeLocationIOService.loadLocations();
        // THEN
        assertThat(result).isNotNull();
        assertThat(result).hasSize(0);
    }

    @AfterEach
    void tearDown() {
        TEST_FILE.delete();
    }

    @AfterAll
    static void afterAll() {
        TEST_FILE.delete();
    }

    private static List<SmokeLocation> getLocationsList() {
        List<SmokeLocation> locations = new ArrayList<>();
        locations.add(new SmokeLocation(DUMMY_ID, DUMMY_WORLD, DUMMY_X, DUMMY_Y, DUMMY_Z, DUMMY_FREQUENCY));
        return locations;
    }

    private static List<SmokeLocation> getEmptyLocationsList() {
        return new ArrayList<>();
    }
}
