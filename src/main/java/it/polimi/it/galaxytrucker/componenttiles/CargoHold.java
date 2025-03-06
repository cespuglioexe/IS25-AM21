package it.polimi.it.galaxytrucker.componenttiles;

public class CargoHold extends ComponentTile {
    private final int containerNumber;
    // private final compatibleCargo;

    // TO DO: list of cargo items in the storage

    public CargoHold(int containers, TileEdge top, TileEdge right, TileEdge bottom, TileEdge left) {
        super(top, right, bottom, left);
        containerNumber = containers;
        // compatibleCargo = {1, 2, 3};
    }

    public int getContainerNumber() {
        return containerNumber;
    }

    /*
    public Cargo removeCargo () {

    }

    public void AddCargo () {

    }
     */
}
