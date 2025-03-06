package it.polimi.it.galaxytrucker.componenttiles;

public class BatteryComponent extends ComponentTile {

    private int batteryCharge;

    public BatteryComponent(int capacity, TileEdge top, TileEdge right, TileEdge bottom, TileEdge left) {
        super(top, right, bottom, left);
        batteryCharge = capacity;
    }

    public int getBatteryCapacity() {
        return batteryCharge;
    }

    public void consumeEnergy() {
        batteryCharge--;
    }

    /* Not sure if this function is needed
    public void addEnergy(int amount) {
        batteryCharge += amount;
    }
    */
}
