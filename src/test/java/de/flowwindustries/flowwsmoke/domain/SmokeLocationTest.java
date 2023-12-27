package de.flowwindustries.flowwsmoke.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SmokeLocationTest {

    @Test
    void verifyWither() {
        var result = new SmokeLocation()
                .withId(1)
                .withWorldName("test")
                .withFrequency(10)
                .withX(0.0d)
                .withY(0.5d)
                .withZ(1.0d);
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getFrequency()).isEqualTo(10);
        assertThat(result.getWorldName()).isEqualTo("test");
        assertThat(result.getX()).isEqualTo(0.0d);
        assertThat(result.getY()).isEqualTo(0.5d);
        assertThat(result.getZ()).isEqualTo(1.0d);
    }
}
