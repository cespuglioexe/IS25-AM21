package it.polimi.it.galaxytrucker.componenttiles;

import it.polimi.it.galaxytrucker.observer.EnergyConsumptionEvent;
import it.polimi.it.galaxytrucker.utility.Direction;

import java.util.List;

/**
 * This class represents a Shield component tile.
 * Shields provide protection to two adjacent sides of the spaceship but require activation.
 *
 * @author Giacomo Amaducci
 * @version 1.1
 */
public class Shield extends ComponentTile implements EnergyConsumer {
    /** Indicates whether the shield is currently active. */
    private boolean isActive;

    /**
     * Each shield covers two adjacent sides of the spaceship.
     * The possible covered side pairs are:
     * - Top + Right
     * - Right + Bottom
     * - Bottom + Left
     * - Left + Top
     * The orientation stores the first direction of the pair.
     */
    private final Direction orientation;

    /**
     * Constructs a new {@code Shield} with the specified orientation and edges.
     *
     * @param dir the initial direction of the shield coverage
     * @param top the type of the top edge of this tile
     * @param right the type of the right edge of this tile
     * @param bottom the type of the bottom edge of this tile
     * @param left the type of the left edge of this tile
     */
    public Shield(Direction dir, TileEdge top, TileEdge right, TileEdge bottom, TileEdge left) {
        super(top, right, bottom, left);
        this.orientation = dir;
        this.isActive = false;
    }

    /**
     * Activates the shield if it is currently inactive.
     */
    @Override
    public void activate() {
        if (!isActive) {
            isActive = true;
            EnergyConsumptionEvent event = new EnergyConsumptionEvent();
            this.publish(event);
        }
    }

    public boolean isActive() {
        return isActive;
    }

    /**
     * Determines if the shield can be activated.
     *
     * @return always {@code false}, as this implementation does not allow reactivation.
     */
    @Override
    public boolean canBeActivated() {
        return false;
    }

    /**
     * Returns the first direction of the pair that is covered by the shield, adjusted for rotation.
     *
     * @return a {@code Direction} representing the first direction of the shield coverage;
     *         the second can be calculated by the caller with
     *         {@code Direction.values()[(@result.ordinal() + 1) % 4]}
     */
    public List<Direction> getOrientation() {
        return List.of(Direction.values()[(orientation.ordinal() + getRotation()) % 4], Direction.values()[(orientation.ordinal() + getRotation() + 1) % 4]);
    }
}
