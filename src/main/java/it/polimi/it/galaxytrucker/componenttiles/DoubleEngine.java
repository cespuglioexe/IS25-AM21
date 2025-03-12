package it.polimi.it.galaxytrucker.componenttiles;

import it.polimi.it.galaxytrucker.observer.EnergyConsumptionEvent;

/**
 * This class represents a Double Engine component tile.
 * Double Engines provide increased propulsion power but require activation.
 * Activation consumes energy and provides a one-time propulsion boost.
 *
 * @author Giacomo Amaducci
 * @version 1.1
 */
public class DoubleEngine extends SingleEngine implements EnergyConsumer {
    /** Indicates whether the double engine is currently active */
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
     * Activation triggers an {@code EnergyConsumptionEvent} and sets the engine to active.
     */
    @Override
    public void activate() {
        if (!isActive) {
            isActive = true;
            EnergyConsumptionEvent event = new EnergyConsumptionEvent();
            this.publish(event);
        }
    }

    /**
     * Returns the power of the engine.
     * If the engine was activated, it provides a power of 2 and then deactivates.
     * If the engine was not activated, it returns 0.
     *
     * @return {@code 2} if the engine was active, otherwise {@code 0}
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
