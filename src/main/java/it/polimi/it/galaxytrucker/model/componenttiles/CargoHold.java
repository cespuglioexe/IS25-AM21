package it.polimi.it.galaxytrucker.model.componenttiles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.model.utility.Cargo;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Cargo Hold component.
 * A cargo hold can store a limited number of cargo items based on its container capacity.
 * This type of cargo hold cannot hold special cargo.
 *
 * @author Giacomo Amaducci
 * @version 1.1
 */
public class CargoHold extends ComponentTile {
    private final int containerNumber;
    boolean canHoldSpecialCargo;
    private List<Cargo> containedCargo;

    /**
     * Constructs a new cargo hold with the specified number of containers and tile edges.
     *
     * @param containers The number of cargo containers this cargo hold can accommodate
     * @param edges The list of edges defining the tile's connections
     */
    @JsonCreator
    public CargoHold(
            @JsonProperty("capacity") int containers,
            @JsonProperty("edges") List<TileEdge> edges,
            @JsonProperty("graphic") String graphicPath) {
        super(edges,graphicPath);
        containerNumber = containers;
        canHoldSpecialCargo = false;
        containedCargo = new ArrayList<>();
    }

    /**
     * Returns the total number of containers this cargo hold can accommodate.
     *
     * @return The maximum number of cargo items this hold can store as an {@code int}
     */
    public int getContainerNumber() {
        return containerNumber;
    }

    /**
     * Returns the list of cargo items currently stored in this cargo hold.
     *
     * @return A {@code List<Cargo>} of cargo items currently in the hold
     */
    public List<Cargo> getContainedCargo() {
        return containedCargo;
    }

    /**
     * Checks if this cargo hold can store special cargo items.
     *
     * @return {@code true} if this cargo hold can store special cargo, {@code false} otherwise
     */
    public boolean canHoldSpecialCargo() {
        return canHoldSpecialCargo;
    }

    /**
     * Adds a new cargo object to the end of the list.
     *
     * @param cargo {@code Cargo} object that needs to be added
     * @throws InvalidActionException if the {@code List<Cargo>} is already at the
     *                                limit of {@code containerNumber} objects
     */
    public void addCargo(Cargo cargo) throws InvalidActionException {
        if (containedCargo.size() >= containerNumber)
            throw new InvalidActionException("CargoHold is already full, can't add cargo");
        containedCargo.add(cargo);
    }

    /**
     * Removes the cargo at the specified index.
     *
     * @param i Index of the {@code Cargo} object that needs to be removed
     * @return The {@code Cargo} object that was just removed from the {@code List<Cargo>}
     * @throws InvalidActionException if the index {@code i} is out of bounds
     */
    public Cargo removeCargo(int i) throws InvalidActionException {
        if (containedCargo.isEmpty())
            throw new InvalidActionException("CargoHold is empty");
        if (i < 0 || i >= containedCargo.size())
            throw new InvalidActionException("Index out of bounds");
        Cargo cargo = containedCargo.get(i);
        containedCargo.remove(i);
        return cargo;
    }
}
