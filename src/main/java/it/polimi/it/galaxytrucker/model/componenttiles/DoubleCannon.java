package it.polimi.it.galaxytrucker.model.componenttiles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * This class represents a Double Cannon component tile.
 * Double Cannons provide increased firepower but require activation.
 * They have higher firepower when facing forward.
 * <p>
 * This class extends {@code SingleCannon} and implements both {@code EnergyConsumer} and {@code Cannon}.
 * </p>
 *
 * @author Giacomo Amaducci
 * @version 1.1
 */
public class DoubleCannon extends SingleCannon implements EnergyConsumer {

    /**
     * Constructs a new {@code DoubleCannon} with the specified edges.
     *
     * @param edges a {@code List<TileEdge>} representing the edges of this tile
     */
    @JsonCreator
    public DoubleCannon(@JsonProperty("edges") List<TileEdge> edges) {
        super(edges);
    }

    /**
     * Activates the double cannon.
     *
     * @return the firepower value of the cannon upon activation
     */
    @Override
    public int activate() {
        return (int) getFirePower();
    }

    /**
     * Returns the firepower of the double cannon.
     * <ul>
     *     <li>If the cannon is facing upwards (rotation {@code 0}), it has a firepower of {@code 2}.</li>
     *     <li>Otherwise, it has a firepower of {@code 1}.</li>
     * </ul>
     *
     * @return a {@code double} representing the firepower of the cannon
     */
    @Override
    public double getFirePower() {
        return (getRotation() == 0) ? 2 : 1;
    }
}