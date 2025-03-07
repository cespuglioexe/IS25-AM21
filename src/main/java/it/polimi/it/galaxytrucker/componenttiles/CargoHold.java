package it.polimi.it.galaxytrucker.componenttiles;
import it.polimi.it.galaxytrucker.utility.Cargo;
import java.util.List;

public class CargoHold extends ComponentTile {
    private final int containerNumber;
    private final boolean canHoldSpecialCargo;
    private List<Cargo> containedCargo;

    public CargoHold(int containers, TileEdge top, TileEdge right, TileEdge bottom, TileEdge left) {
        super(top, right, bottom, left);
        containerNumber = containers;
        this.canHoldSpecialCargo = false;
    }

    public int getContainerNumber() {
        return containerNumber;
    }

    public boolean isCanHoldSpecialCargo() {
        return canHoldSpecialCargo;
    }

    public void removeCargo (Cargo cargo) {
        containedCargo.remove(cargo);
    }

    public void addCargo (Cargo cargo) {
        containedCargo.add(cargo);
    }

    public List<Cargo> getContainedCargo() {
        return containedCargo;
    }
}
