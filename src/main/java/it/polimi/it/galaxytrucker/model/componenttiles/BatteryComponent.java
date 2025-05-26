package it.polimi.it.galaxytrucker.model.componenttiles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;

import java.util.List;

/**
 * Represents a Battery Component.
 * This component provides energy storage with a limited capacity.
 * The battery charge can be consumed or lost during the course of a game.
 *
 * @author Giacomo Amaducci
 * @version 1.1
 */
public class BatteryComponent extends ComponentTile {

    private int batteryCharge;

    /**
     * Constructs a new {@code BatteryComponent} with the specified capacity and tile edges.
     *
     * @param capacity The initial charge capacity of the battery.
     * @param edges A list representing the edges of the tile.
     */
    @JsonCreator
    public BatteryComponent(
            @JsonProperty("capacity") int capacity,
            @JsonProperty("edges") List<TileEdge> edges,
            @JsonProperty("graphic") String graphicPath) {
        super(edges,graphicPath);
        batteryCharge = capacity;
    }

    /**
     * Returns the current amount of charge left in the battery.
     *
     * @return an {@code int} representing the current charge of the battery.
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
