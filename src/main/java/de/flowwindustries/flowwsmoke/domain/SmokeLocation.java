package de.flowwindustries.flowwsmoke.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.io.Serial;
import java.io.Serializable;

/**
 * Represents an in-game location where smoke particles will be spawned.
 */
@Data
@With
@NoArgsConstructor
@AllArgsConstructor
public class SmokeLocation implements Serializable {

    @Serial
    private static final long serialVersionUID = 3218173L;

    /**
     * Identifier.
     */
    private Integer id;

    /**
     * Geographical location in world.
     */
    private String worldName;

    /**
     * X-Coordinate
     */
    private Double x;

    /**
     * Y-Coordinate
     */
    private Double y;

    /**
     * Z-Coordinate
     */
    private Double z;
}
