package it.polimi.it.galaxytrucker.componenttiles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * This class represents a Structural Module component tile.
 * Structural Module components have no functionality and are only used as
 * connectors between other components.
 *
 * @author Giacomo Amaducci
 * @version 1.1
 */
public class StructuralModule extends ComponentTile {
    /**
     * Constructs a new Structural Module with the specified edges.
     *
     * @param edges a {@code List} of {@link TileEdge} objects representing the four edges of this tile
     */
    @JsonCreator
    public StructuralModule(@JsonProperty("edges") List<TileEdge> edges) {
        super(edges);
    }
}