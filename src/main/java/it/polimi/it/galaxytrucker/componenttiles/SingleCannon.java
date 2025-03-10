package it.polimi.it.galaxytrucker.componenttiles;

import java.util.List;

/**
 * This class represents a Single Cannon component tile.
 * Single Cannons provide offensive capabilities with a firepower of 1.
 *
 * @author Giacomo Amaducci
 * @version 1.0
 */
public class SingleCannon extends ComponentTile {

    /**
     * The firepower value of this cannon.
     */
    private int firePower;

    /**
     * Constructs a new {@code SingleCannon} with the specified edges.
     * The firepower is always initialized to 1.
     *
     * @param top the type of the top edge of this tile
     * @param right the type of the right edge of this tile
     * @param bottom the type of the bottom edge of this tile
     * @param left the type of the left edge of this tile
     */
    public SingleCannon(TileEdge top, TileEdge right, TileEdge bottom, TileEdge left) {
        super(top, right, bottom, left);
        firePower = 1;
    }

    /**
     * Returns the firepower of the cannon.
     * For single cannons, this value is 1 if it points forwards, 0.5 otherwise.
     *
     * @return an {@code int} representing the firepower of the cannon
     */
    public double getFirePower() {
        if (getRotation() == 0)
            return firePower;
        return firePower * 0.5;
    }

    /**
     * Sets the firepower value of the cannon.
     *
     * @param firePower the new {@code int} value to set for the firepower
     */
    void setFirePower(int firePower) {
        this.firePower = firePower;
    }
}
