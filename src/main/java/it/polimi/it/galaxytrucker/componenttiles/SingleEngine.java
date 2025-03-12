package it.polimi.it.galaxytrucker.componenttiles;

/**
 * This class represents a Single Engine component tile.
 * Single Engines provide propulsion capabilities with an engine power of 1.
 *
 * @author Giacomo Amaducci
 * @version 1.0
 */
public class SingleEngine extends ComponentTile {

    /** The power value of this engine */
    private final int enginePower;

    /**
     * Constructs a new {@code SingleEngine} with the specified edges.
     * The engine power is always initialized to 1.
     *
     * @param top the type of the top edge of this tile
     * @param right the type of the right edge of this tile
     * @param bottom the type of the bottom edge of this tile
     * @param left the type of the left edge of this tile
     */
    public SingleEngine(TileEdge top, TileEdge right, TileEdge bottom, TileEdge left) {
        super(top, right, bottom, left);
        enginePower = 1;
    }

    /**
     * Returns the power of the engine.
     * For single engines, this value is always 1.
     *
     * @return an {@code int} representing the power of the engine
     */
    public int getEnginePower() {
        return enginePower;
    }
}
