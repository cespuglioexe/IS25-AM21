package it.polimi.it.galaxytrucker.model.componenttiles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * This class represents a Special Cargo Hold component tile.
 * Special Cargo Holds can store containers of goods including special cargo.
 *
 * @author Giacomo Amaducci
 * @version 1.1
 */
public class SpecialCargoHold extends CargoHold {
    /**
     * Constructs a new Special Cargo Hold with the specified number of containers and edges.
     * The hold is automatically configured to be able to hold special cargo.
     *
     * @param containers the number of containers this cargo hold can store
     * @param edges a {@code List} of {@link TileEdge} objects representing the four edges of this tile
     */
    @JsonCreator
    public SpecialCargoHold(
            @JsonProperty("containerNum") int containers,
            @JsonProperty("edges")List<TileEdge> edges,
            @JsonProperty("graphic") String graphicPath) {
        super(containers, edges, graphicPath);
        this.canHoldSpecialCargo = true;
    }
}