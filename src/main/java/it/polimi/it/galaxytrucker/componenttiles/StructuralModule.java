package it.polimi.it.galaxytrucker.componenttiles;

/**
 * This class represents a Structural Module component tile.
 * Structural Module components have no functionality and are only used as
 * connectors between other components.
 *
 * @author Giacomo Amaducci
 * @version 1.0
 */
public class StructuralModule extends ComponentTile {
    /**
     * Constructs a new Structural Module with the specified edges.
     *
     * @param top the type of the top edge of this tile
     * @param right the type of the right edge of this tile
     * @param bottom the type of the bottom edge of this tile
     * @param left the type of the left edge of this tile
     */
    public StructuralModule(TileEdge top, TileEdge right, TileEdge bottom, TileEdge left) {
        super(top, right, bottom, left);
    }
}
