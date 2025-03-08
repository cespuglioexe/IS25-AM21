package it.polimi.it.galaxytrucker.componenttiles;

import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;

public class BatteryComponent extends ComponentTile {

    private int batteryCharge;

    public BatteryComponent(int capacity, TileEdge top, TileEdge right, TileEdge bottom, TileEdge left) {
        super(top, right, bottom, left);
        batteryCharge = capacity;
    }

    /*
    *   Returns the current amount of charge left in the battery
    *
    *   @return An {@code int} representing the current charge of the battery.
    *           This value is between the initial charge set in the constructor (either 2 or 3) and 0.
    */
    public int getBatteryCapacity() {
        return batteryCharge;
    }

    /*
    *   Consumes energy by reducing batteryCharge by 1
    *
    *   @throws InvalidActionException if {@code batteryCharge} is already at the minimum.
    */
    public void consumeEnergy() throws InvalidActionException {
        if (batteryCharge <= 0)
            throw new InvalidActionException("Battery charge must be greater than zero");
        batteryCharge--;
    }
}
