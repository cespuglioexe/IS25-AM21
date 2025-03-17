package it.polimi.it.galaxytrucker.componenttiles;

import it.polimi.it.galaxytrucker.observer.EnergyConsumptionEvent;

import java.util.List;

/**
 * This class represents a Double Cannon component tile.
 * Double Cannons provide increased firepower but require activation.
 *
 * @author Giacomo Amaducci
 * @version 1.1
 */
public class DoubleCannon extends SingleCannon implements EnergyConsumer, Cannon {
    private boolean isActive;

    /*
     * Constructs a new {@code DoubleCannon} with the specified edges.
     *
     * @param top the type of the top edge of this tile
     * @param right the type of the right edge of this tile
     * @param bottom the type of the bottom edge of this tile
     * @param left the type of the left edge of this tile
     */
    public DoubleCannon(List<TileEdge> edges) {
        super(edges);
        this.isActive = false;
    }

    /**
     * Activates the double cannon if it is currently inactive.
     */
    @Override
    public int activate() {
        if (!isActive) {
            isActive = true;
            EnergyConsumptionEvent event = new EnergyConsumptionEvent();
            this.publish(event);
        }
        return -1;
    }

    /**
     * Returns the firepower of the cannon.
     * For activated double cannons, this value is 2 if it points forwards, 1 otherwise.
     * For inactive double cannons, this value is always 0.
     *
     * @return a {@code double} representing the firepower of the cannon
     */
    @Override
    public double getFirePower() {
        if (!isActive)
            return 0;
        if (getRotation() == 0) {
            isActive = false;
            return 2;
        }
        isActive = false;
        return 1;
    }
}