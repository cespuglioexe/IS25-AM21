package it.polimi.it.galaxytrucker.model.componenttiles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.it.galaxytrucker.model.utility.Direction;

import java.util.List;

/**
 * This class represents a Shield component tile.
 * Shields provide protection to two adjacent sides of the spaceship but require activation.
 *
 * @author Giacomo Amaducci
 * @version 1.3
 */
public class Shield extends ComponentTile implements EnergyConsumer {
    /**
     * Each shield covers two adjacent sides of the spaceship.
     * The possible covered side pairs are:
     * - Top + Right
     * - Right + Bottom
     * - Bottom + Left
     * - Left + Top
     * The orientation stores the first direction of the pair.
     */
    private final Direction orientation;

    /**
     * Constructs a new {@code Shield} with the specified orientation and edges.
     *
     * @param dir the initial direction of the shield coverage
     * @param edges a {@code List} of {@link TileEdge} objects representing the four edges of this tile
     */
    @JsonCreator
    public Shield(
            @JsonProperty("direction") Direction dir,
            @JsonProperty("edges") List<TileEdge> edges,
            @JsonProperty("graphic") String graphicPath) {
        super(edges,graphicPath);
        this.orientation = dir;
    }

    /**
     * Activates the shield if it is currently inactive.
     *
     * @return -1 as the energy consumption value
     */
    @Override
    public int activate() {
        return 0;
    }

    /**
     * Returns the pair of directions that is covered by the shield, adjusted for rotation.
     *
     * @return a {@code List<Direction>} representing the directions of the shield coverage
     */
    public List<Direction> getOrientation() {
        return List.of(Direction.values()[(orientation.ordinal() + getRotation()) % 4], Direction.values()[(orientation.ordinal() + getRotation() + 1) % 4]);
    }
}