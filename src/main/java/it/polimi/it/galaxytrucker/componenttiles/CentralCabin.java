
package it.polimi.it.galaxytrucker.componenttiles;

import it.polimi.it.galaxytrucker.crewmates.Alien;
import it.polimi.it.galaxytrucker.crewmates.Crewmate;
import it.polimi.it.galaxytrucker.crewmates.Human;
import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;

import java.util.ArrayList;
import java.util.List;

public class CentralCabin extends ComponentTile {
    List<Crewmate> crewmates;

    public CentralCabin(TileEdge top, TileEdge right, TileEdge bottom, TileEdge left) {
        super(top, right, bottom, left);
        crewmates = new ArrayList<>();
    }

    public List<Crewmate> getCrewmates() {
        return crewmates;
    }

    /*
     *   Adds a new {@code Human} object to the {@code List<Crewmate>} of the cabin.
     *
     *   @throws InvalidActionException if the cabin is already full.
     */
    public void addCrewmate (Human human) throws InvalidActionException {
        if (crewmates.size() == 2)
            throw new InvalidActionException("Cabin already full");
        else if (!crewmates.isEmpty() && crewmates.getFirst().getClass().equals(Alien.class))
            throw new InvalidActionException("Cabin contains Alien, can't add Human");
        crewmates.add(human);
    }

    /*
     *   Removes a {@code Crewmate} from the {@code List<Crewmate>} of the cabin, the first element
     *   of the {@code List<Crewmate>} is removed for simplicity.
     *
     *   @throws InvalidActionException if the cabin is empty.
     */
    public Crewmate removeCrewmate () throws InvalidActionException {
        if (crewmates.isEmpty())
            throw new InvalidActionException("Cabin is empty");

        Crewmate removedCrewmate = crewmates.getFirst();
        crewmates.removeFirst();
        return removedCrewmate;
    }
}
