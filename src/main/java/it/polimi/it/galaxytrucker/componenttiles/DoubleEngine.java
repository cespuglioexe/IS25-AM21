package it.polimi.it.galaxytrucker.componenttiles;

/**
 * This class represents a Double Engine component tile.
 * Double Engines provide increased propulsion power but require activation.
 *
 * @author Giacomo Amaducci
 * @version 1.0
 */
public class DoubleEngine extends SingleEngine implements EnergyConsumer {

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
    }

    /**
     * Activates the double engine by setting its engine power to 2 if it is currently inactive.
     */
    @Override
    public void activate() {
        if (getEnginePower() == 0) {
            setEnginePower(2);
        }
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
