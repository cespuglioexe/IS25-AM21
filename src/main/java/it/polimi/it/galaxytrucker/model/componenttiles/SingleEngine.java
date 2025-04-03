package it.polimi.it.galaxytrucker.model.componenttiles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * This class represents a Single Engine component tile.
 * Single Engines provide propulsion capabilities with an engine power of 1.
 *
 * @author Giacomo Amaducci
 * @version 1.1
 */
public class SingleEngine extends ComponentTile {

    /** The power value of this engine */
    private final int enginePower;

    /**
     * Constructs a new {@code SingleEngine} with the specified edges.
     * The engine power is always initialized to 1.
     *
     * @param edges a {@code List} of {@link TileEdge} objects representing the four edges of this tile
     */
    @JsonCreator
    public SingleEngine(@JsonProperty("edges") List<TileEdge> edges) {
        super(edges);
        enginePower = 1;
    }

    /**
     * Returns the power of the engine.
     * For single engines, this value is {@code 1} if it points downwards, {@code 0} otherwise.
     *
     * @return an {@code int} representing the power of the engine
     */
    public int getEnginePower() {
        return (getRotation() == 0) ? enginePower : 0;
    }
}