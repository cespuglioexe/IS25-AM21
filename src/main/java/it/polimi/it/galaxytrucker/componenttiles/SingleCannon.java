package it.polimi.it.galaxytrucker.componenttiles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * This class represents a Single Cannon component tile.
 * Single Cannons provide offensive capabilities with a firepower of 1.
 *
 * @author Giacomo Amaducci
 * @version 1.1
 */
public class SingleCannon extends ComponentTile implements Cannon {

    /** The firepower value of this cannon */
    private final int firePower;

    /**
     * Constructs a new {@code SingleCannon} with the specified edges.
     * The firepower is always initialized to 1.
     *
     * @param edges a {@code List} of {@link TileEdge} objects representing the four edges of this tile
     */
    @JsonCreator
    public SingleCannon(@JsonProperty("edges") List<TileEdge> edges) {
        super(edges);
        firePower = 1;
    }

    /**
     * Returns the firepower of the cannon.
     * For single cannons, this value is {@code 1} if it points forwards, {@code 0.5} otherwise.
     *
     * @return a {@code double} representing the firepower of the cannon
     */
    public double getFirePower() {
        if (getRotation() == 0)
            return firePower;
        return firePower * 0.5;
    }
}