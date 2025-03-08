package it.polimi.it.galaxytrucker.componenttiles;

/**
 * This class represents an Empty Tile component, a space outside the ship where no component can be placed.
 * An Empty Tile has all smooth.
 *
 * @author Giacomo Amaducci
 * @version 1.0
 */
public class EmptyTile extends ComponentTile {
    /**
     * Constructs a new Empty Tile.
     * All edges are initialized as SMOOTH.
     */
    public EmptyTile() {
        super(TileEdge.SMOOTH, TileEdge.SMOOTH, TileEdge.SMOOTH, TileEdge.SMOOTH);
    }
}