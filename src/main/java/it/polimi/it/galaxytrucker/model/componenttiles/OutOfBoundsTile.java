package it.polimi.it.galaxytrucker.model.componenttiles;

import java.util.List;

/**
 * This class represents an Out Of Bounds component, a space outside the ship where no component can be placed.
 * An Empty Tile has all edges set to {@code SMOOTH}.
 *
 * @author Giacomo Amaducci
 * @version 1.1
 */
public class OutOfBoundsTile extends ComponentTile {
    /**
     * Constructs a new Empty Tile.
     * All edges are initialized as {@code SMOOTH}.
     */
    public OutOfBoundsTile() {
        super(List.of(TileEdge.SMOOTH, TileEdge.SMOOTH, TileEdge.SMOOTH, TileEdge.SMOOTH));
    }
}