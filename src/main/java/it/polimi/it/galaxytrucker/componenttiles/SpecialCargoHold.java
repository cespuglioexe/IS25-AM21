package it.polimi.it.galaxytrucker.componenttiles;

import java.util.List;

/**
 * This class represents a Special Cargo Hold component tile.
 * Special Cargo Holds can store containers of goods including special cargo.
 *
 * @author Giacomo Amaducci
 * @version 1.0
 */
public class SpecialCargoHold extends CargoHold {
    /*
     * Constructs a new Special Cargo Hold with the specified number of containers and edges.
     * The hold is automatically configured to be able to hold special cargo.
     *
     * @param containers the number of containers this cargo hold can store
     * @param top the type of the top edge of this tile
     * @param right the type of the right edge of this tile
     * @param bottom the type of the bottom edge of this tile
     * @param left the type of the left edge of this tile
     */
    public SpecialCargoHold(int containers, List<TileEdge> edges) {
        super(containers, edges);
        this.canHoldSpecialCargo = true;
    }
}