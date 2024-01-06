package de.flowwindustries.flowwsmoke.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

/**
 * Represents an in-game location where smoke particles will be spawned.
 */
@Data
@With
@NoArgsConstructor
@AllArgsConstructor
public class SmokeLocation {
    private Integer id;
    private String worldName;
    private Double x;
    private Double y;
    private Double z;
    private double offsetX;
    private double offsetY;
    private double offsetZ;
    private Integer frequency;
}
