package it.polimi.it.galaxytrucker.componenttiles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


/**
 * This class represents a Double Engine component tile.
 * Double Engines provide increased propulsion power but require activation.
 * <p>
 * This class extends {@code SingleEngine} and implements {@code EnergyConsumer}.
 * </p>
 *
 * @author Giacomo Amaducci
 * @version 1.1
 */
public class DoubleEngine extends SingleEngine implements EnergyConsumer {

    /**
     * Constructs a new {@code DoubleEngine} with the specified edges.
     *
     * @param edges a {@code List<TileEdge>} representing the edges of this tile
     */
    @JsonCreator
    public DoubleEngine(@JsonProperty("edges") List<TileEdge> edges) {
        super(edges);
    }

    /**
     * Activates the double engine.
     *
     * @return the power of the engine
     */
    @Override
    public int activate() {
        return getEnginePower();
    }

    /**
     * Returns the power of the double engine.
     *<p>
     *  Engine power is {@code 2} when the engine in pointing downwards (rotation {@code 0}), {@code 0} otherwise.
     *</p>
     * @return an {@code int} representing the engine power
     */
    @Override
    public int getEnginePower() {
        return getRotation() == 0 ? 2 :0;
    }
}
