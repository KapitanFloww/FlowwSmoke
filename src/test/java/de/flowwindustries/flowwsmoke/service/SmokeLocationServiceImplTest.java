package de.flowwindustries.flowwsmoke.service;

import de.flowwindustries.flowwsmoke.domain.SmokeLocation;
import de.flowwindustries.flowwsmoke.domain.SmokeLocationDTO;
import de.flowwindustries.flowwsmoke.service.impl.SmokeLocationServiceImpl;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class SmokeLocationServiceImplTest {

    public static final Double DUMMY_X = 1.0d;
    public static final Double DUMMY_Y = 2.0d;
    public static final Double DUMMY_Z = 3.0d;
    public static final String DUMMY_WORLD = "dummyWorld";
    private static final String DUMMY_WORLD_2 = "dummyWorld_2";

    // Unit-under-test
    private final SmokeLocationService smokeLocationService;
    private final SmokeLocationIOService smokeLocationIOService;

    public SmokeLocationServiceImplTest() {
        this.smokeLocationIOService = mock(SmokeLocationIOService.class);
        this.smokeLocationService = new SmokeLocationServiceImpl(this.smokeLocationIOService);
    }

    @Test
    void verifyAddSmoke() {
        // GIVEN
        // WHEN
        int id = smokeLocationService.addSmoke(getDummyDTO(DUMMY_WORLD));
        // THEN
        assertThat(id).isNotNull();
        assertThat(smokeLocationService.getAll(null)).hasSize(1);
        assertSmokeLocation(smokeLocationService.getAll(null).get(0), id, DUMMY_WORLD);
    }

    @Test
    void verifyAddSmokeCounterIncrement() {
        // GIVEN
        int id1 = smokeLocationService.addSmoke(getDummyDTO(DUMMY_WORLD));
        // WHEN
        int id2 = smokeLocationService.addSmoke(getDummyDTO(DUMMY_WORLD));
        // THEN
        assertThat(id1).isNotEqualTo(id2);
        assertThat(id2).isEqualTo(id1 + 1);
    }

    @Test
    void verifyAddSmokeCounterIncrementWhenRemove() {
        // GIVEN
        int id1 = smokeLocationService.addSmoke(getDummyDTO(DUMMY_WORLD));
        // WHEN
        // remove smoke with - should NOT reset the counter
        smokeLocationService.deleteSmoke(id1);
        // add new smoke
        int id2 = smokeLocationService.addSmoke(getDummyDTO(DUMMY_WORLD));
        // THEN
        assertThat(id2).isEqualTo(id1 + 1);
    }

    @Test
    void verifyAddSmokePersistsLocations() {
        // GIVEN
        // WHEN
        smokeLocationService.addSmoke(getDummyDTO(DUMMY_WORLD));
        // THEN
        verify(smokeLocationIOService, times(1)).persistLocations(anyList());
    }

    @Test
    void verifyGetSmoke() {
        // GIVEN
        int id = smokeLocationService.addSmoke(getDummyDTO(DUMMY_WORLD));
        // WHEN
        SmokeLocation smokeLocation = smokeLocationService.getSmoke(id);
        // THEN
        assertSmokeLocation(smokeLocation, id, DUMMY_WORLD);
    }

    @Test
    void verifyGetNonExistingSmoke() {
        // GIVEN
        // WHEN
        SmokeLocation smokeLocation = smokeLocationService.getSmoke(Integer.MIN_VALUE); // not existing
        // THEN
        assertThat(smokeLocation).isNull();
    }

    @Test
    void verifyGetAll() {
        // GIVEN
        int id = smokeLocationService.addSmoke(getDummyDTO(DUMMY_WORLD));
        // WHEN
        var result = smokeLocationService.getAll(null);
        // THEN
        assertThat(result).hasSize(1);
        assertSmokeLocation(result.get(0), id, DUMMY_WORLD);
    }

    @Test
    void verifyGetAllMultipleEntries() {
        // GIVEN
        int id = smokeLocationService.addSmoke(getDummyDTO(DUMMY_WORLD));
        int id2 = smokeLocationService.addSmoke(getDummyDTO(DUMMY_WORLD));
        // WHEN
        var result = smokeLocationService.getAll(null);
        // THEN
        assertThat(result).hasSize(2);
        assertSmokeLocation(result.get(0), id, DUMMY_WORLD);
        assertSmokeLocation(result.get(1), id2, DUMMY_WORLD);
    }

    @Test
    void verifyGetAllWorld() {
        // GIVEN
        int id = smokeLocationService.addSmoke(getDummyDTO(DUMMY_WORLD));
        smokeLocationService.addSmoke(getDummyDTO(DUMMY_WORLD_2));
        // WHEN
        var result = smokeLocationService.getAll("dummyWorld");
        // THEN
        assertThat(result).hasSize(1);
        assertSmokeLocation(result.get(0), id, DUMMY_WORLD);
    }

    @Test
    void verifyGetAllWorldNull() {
        // GIVEN
        int id1 = smokeLocationService.addSmoke(getDummyDTO(DUMMY_WORLD));
        int id2 = smokeLocationService.addSmoke(getDummyDTO(DUMMY_WORLD_2));
        // WHEN
        var result = smokeLocationService.getAll(null); // Should return all entries
        // THEN
        assertThat(result).hasSize(2);
        assertSmokeLocation(result.get(0), id1, DUMMY_WORLD);
        assertSmokeLocation(result.get(1), id2, DUMMY_WORLD_2);
    }

    @Test
    void verifyGetAllEmpty() {
        assertThat(smokeLocationService.getAll(null)).isEmpty();
    }

    @Test
    void verifyDeleteSmoke() {
        // GIVEN
        int id = smokeLocationService.addSmoke(getDummyDTO(DUMMY_WORLD));
        // WHEN
        smokeLocationService.deleteSmoke(id);
        // THEN
        assertThat(smokeLocationService.getSmoke(id)).isNull();
    }

    @Test
    void verifyDeleteNotExistingSmoke() {
        assertThatNoException()
                .isThrownBy(() -> smokeLocationService.deleteSmoke(Integer.MIN_VALUE));
    }

    @Test
    void verifyDeletePersistsLocations() {
        // GIVEN
        int id = smokeLocationService.addSmoke(getDummyDTO(DUMMY_WORLD));
        // WHEN
        smokeLocationService.deleteSmoke(id);
        // THEN
        verify(smokeLocationIOService, times(2)).persistLocations(anyList()); // 1 add + 1 delete
    }

    @Test
    void verifyDeleteAllNull() {
        // GIVEN
        smokeLocationService.addSmoke(getDummyDTO(DUMMY_WORLD));
        smokeLocationService.addSmoke(getDummyDTO(DUMMY_WORLD_2));
        // WHEN
        smokeLocationService.deleteAll(null);
        // THEN
        assertThat(smokeLocationService.getAll(null)).isEmpty();
    }

    @Test
    void verifyDeleteAllNullPersistsLocations() {
        // GIVEN
        smokeLocationService.addSmoke(getDummyDTO(DUMMY_WORLD));
        smokeLocationService.addSmoke(getDummyDTO(DUMMY_WORLD));
        // WHEN
        smokeLocationService.deleteAll(null);
        // THEN
        verify(smokeLocationIOService, times(3)).persistLocations(anyList()); // 2 add + 1 delete
    }

    @Test
    void verifyDeleteAllWorld() {
        // GIVEN
        smokeLocationService.addSmoke(getDummyDTO(DUMMY_WORLD));
        int id2 = smokeLocationService.addSmoke(getDummyDTO(DUMMY_WORLD_2));
        // WHEN
        smokeLocationService.deleteAll(DUMMY_WORLD);
        // THEnN
        var result = smokeLocationService.getAll(null); // Should return all entries
        assertThat(result).hasSize(1);
        assertSmokeLocation(result.get(0), id2, DUMMY_WORLD_2);
    }

    @Test
    void verifyDeleteAllWorldPersistsLocations() {
        // GIVEN
        smokeLocationService.addSmoke(getDummyDTO(DUMMY_WORLD));
        smokeLocationService.addSmoke(getDummyDTO(DUMMY_WORLD));
        // WHEN
        smokeLocationService.deleteAll(DUMMY_WORLD);
        // THEN
        verify(smokeLocationIOService, times(3)).persistLocations(anyList()); // 2 add + 1 delete
    }

    private static SmokeLocationDTO getDummyDTO(String worldName) {
        return new SmokeLocationDTO()
                .withWorldName(worldName)
                .withX(DUMMY_X)
                .withY(DUMMY_Y)
                .withZ(DUMMY_Z);
    }

    protected static void assertSmokeLocation(SmokeLocation smokeLocation, int id, String worldName) {
        assertThat(smokeLocation).isNotNull();
        assertThat(smokeLocation.getId()).isEqualTo(id);
        assertThat(smokeLocation.getWorldName()).isEqualTo(worldName);
        assertThat(smokeLocation.getX()).isEqualTo(DUMMY_X);
        assertThat(smokeLocation.getY()).isEqualTo(DUMMY_Y);
        assertThat(smokeLocation.getZ()).isEqualTo(DUMMY_Z);
    }
}
