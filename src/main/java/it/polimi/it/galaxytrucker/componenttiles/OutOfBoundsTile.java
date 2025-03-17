package it.polimi.it.galaxytrucker.componenttiles;

import java.util.List;

/**
 * This class represents an Empty Tile component, a space outside the ship where no component can be placed.
 * An Empty Tile has all smooth.
 *
 * @author Giacomo Amaducci
 * @version 1.0
 */
public class OutOfBoundsTile extends ComponentTile {
    /**
     * Constructs a new Empty Tile.
     * All edges are initialized as SMOOTH.
     */
    public OutOfBoundsTile() {
        super(List.of(TileEdge.SMOOTH, TileEdge.SMOOTH, TileEdge.SMOOTH, TileEdge.SMOOTH));
    }
}