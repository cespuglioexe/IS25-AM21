package it.polimi.it.galaxytrucker.componenttiles;

import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.utility.Cargo;
import java.util.List;

public class CargoHold extends ComponentTile {
    private final int containerNumber;
    private boolean canHoldSpecialCargo;
    private List<Cargo> containedCargo;

    public CargoHold(int containers, TileEdge top, TileEdge right, TileEdge bottom, TileEdge left) {
        super(top, right, bottom, left);
        containerNumber = containers;
        setCanHoldSpecialCargo(false);
    }

    public int getContainerNumber() {
        return containerNumber;
    }

    public List<Cargo> getContainedCargo() {
        return containedCargo;
    }

    public boolean isCanHoldSpecialCargo() {
        return canHoldSpecialCargo;
    }

    public void setCanHoldSpecialCargo(boolean canHoldSpecialCargo) {
        this.canHoldSpecialCargo = canHoldSpecialCargo;
    }

    /*
     *   Removes the cargo at the specified index
     *
     *   @param i    Index of the {@code Cargo} object that need to be removed
     *   @return     The {@code Cargo} object at index {@code i} that was just removed
     *               from the {@code List<Cargo>}
     *   @throw      InvalidActionException if the index {@code i} is out of bounds
     */
    public Cargo removeCargo (int i) throws InvalidActionException {
        if (i < 0 || i >= containedCargo.size())
            throw new InvalidActionException("CargoHold can't be empty when removing cargo");
        Cargo cargo = containedCargo.get(i);
        containedCargo.remove(i);
        return cargo;
    }

    /*
     *   Adds a new cargo object to the end of the list
     *
     *   @param cargo   {@code Cargo} object that needs to be added
     *   @throw         InvalidActionException if {@code List<Cargo>} is already at the
     *                  limit of {@code containerNumber} objects
     */
    public void addCargo (Cargo cargo) throws InvalidActionException {
        if (containedCargo.size() >= containerNumber)
            throw new InvalidActionException("CargoHold is already full, can't add cargo");
        containedCargo.add(cargo);
    }
}
