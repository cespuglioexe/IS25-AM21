package it.polimi.it.galaxytrucker.componenttiles;

import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;

/**
 * Represents a Battery Component.
 * This component provides energy storage with a limited capacity.
 * The battery charge can be consumed or lost during the course of a game.
 *
 * @author Giacomo Amaducci
 * @version 1.0
 */
public class BatteryComponent extends ComponentTile {

    private int batteryCharge;

    /**
     * Constructs a new battery component with the specified capacity and tile edges.
     *
     * @param capacity The initial charge capacity of the battery
     * @param top The type of edge on the top side of this tile
     * @param right The type of edge on the right side of this tile
     * @param bottom The type of edge on the bottom side of this tile
     * @param left The type of edge on the left side of this tile
     */
    public BatteryComponent(int capacity, TileEdge top, TileEdge right, TileEdge bottom, TileEdge left) {
        super(top, right, bottom, left);
        batteryCharge = capacity;
    }

    /**
     * Returns the current amount of charge left in the battery.
     *
     * @return An {@code int} representing the current charge of the battery.
     *         This value is between the initial charge set in the constructor (either 2 or 3) and 0.
     */
    public int getBatteryCapacity() {
        return batteryCharge;
    }

    /**
     * Consumes energy by reducing {@code batteryCharge} by 1.
     *
     * @throws InvalidActionException if {@code batteryCharge} is already at 0 or below.
     */
    public void consumeEnergy() throws InvalidActionException {
        if (batteryCharge <= 0)
            throw new InvalidActionException("Battery charge must be greater than zero");
        batteryCharge--;
    }
}