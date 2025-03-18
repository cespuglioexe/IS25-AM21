package it.polimi.it.galaxytrucker.componenttiles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.it.galaxytrucker.crewmates.Alien;
import it.polimi.it.galaxytrucker.crewmates.Crewmate;
import it.polimi.it.galaxytrucker.crewmates.Human;
import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a Central Cabin component tile.
 * Central Cabins can hold crew members (only humans) up to a maximum capacity.
 *
 * @author Giacomo Amaducci
 * @version 1.1
 */
public class CentralCabin extends ComponentTile {
    /** List of crewmates currently in this cabin */
    List<Crewmate> crewmates;

    /**
     * Constructs a new Central Cabin with the specified edges.
     * Initializes an empty crewmates list.
     *
     * @param edges the list of edges defining the tile connections
     */
    @JsonCreator
    public CentralCabin(@JsonProperty("edges") List<TileEdge> edges) {
        super(edges);
        crewmates = new ArrayList<>();
    }

    /**
     * Returns the list of crewmates currently in this cabin.
     *
     * @return the {@code List<Crewmate>} containing all crewmates in this cabin
     */
    public List<Crewmate> getCrewmates() {
        return crewmates;
    }

    /**
     * Adds a new {@code Human} object to the {@code List<Crewmate>} of the cabin.
     * The cabin can hold a maximum of 2 crewmates.
     * Humans cannot be added to a cabin that contains an Alien.
     *
     * @param human the human to be added to the cabin
     * @throws InvalidActionException if the cabin is already full (contains 2 crewmates) or
     *                               if the cabin contains an Alien
     */
    public void addCrewmate(Human human) throws InvalidActionException {
        if (crewmates.size() == 2)
            throw new InvalidActionException("Cabin already full");
        else if (!crewmates.isEmpty() && crewmates.get(0).getClass().equals(Alien.class))
            throw new InvalidActionException("Cabin contains Alien, can't add Human");
        crewmates.add(human);
    }

    /**
     * Removes a {@code Crewmate} from the {@code List<Crewmate>} of the cabin.
     * For simplicity, the first element of the {@code List<Crewmate>} is removed.
     *
     * @return the {@code Crewmate} that was removed from the cabin
     * @throws InvalidActionException if the cabin is empty
     */
    public Crewmate removeCrewmate() throws InvalidActionException {
        if (crewmates.isEmpty())
            throw new InvalidActionException("Cabin is empty");

        return crewmates.remove(0);
    }
}
