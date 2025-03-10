package it.polimi.it.galaxytrucker.componenttiles;

/**
 * This class represents a Double Engine component tile.
 * Double Engines provide increased propulsion power but require activation.
 *
 * @author Giacomo Amaducci
 * @version 1.0
 */
public class DoubleEngine extends SingleEngine implements EnergyConsumer {
    private boolean isActive;

    /**
     * Constructs a new {@code DoubleEngine} with the specified edges.
     *
     * @param top the type of the top edge of this tile
     * @param right the type of the right edge of this tile
     * @param bottom the type of the bottom edge of this tile
     * @param left the type of the left edge of this tile
     */
    public DoubleEngine(TileEdge top, TileEdge right, TileEdge bottom, TileEdge left) {
        super(top, right, bottom, left);
        isActive = false;
    }

    /**
     * Activates the double engine if it is currently inactive.
     */
    @Override
    public void activate() {
        if (!isActive)
            isActive = true;
        // CONSUME ENERGY ?
    }

    /**
     * Returns the power of the engine.
     * For double engines, this value is 2 if it is activated, 0 otherwise.
     *
     * @return an {@code int} representing the power of the engine
     */
    @Override
    public int getEnginePower() {
        if (!isActive)
            return 0;
        isActive = false;
        return 2;
    }

    /**
     * Determines if the engine can be activated.
     *
     * @return always {@code false}, as this implementation does not allow reactivation.
     */
    @Override
    public boolean canBeActivated() {
        return false;
    }
}
