package de.flowwindustries.flowwsmoke.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

/**
 * DTO for {@link SmokeLocation}s.
 */
@Data
@With
@NoArgsConstructor
@AllArgsConstructor
public class SmokeLocationDTO {
    private String worldName;
    private Double x;
    private Double y;
    private Double z;
}
