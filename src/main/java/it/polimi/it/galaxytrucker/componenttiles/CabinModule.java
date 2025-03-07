package it.polimi.it.galaxytrucker.componenttiles;

import it.polimi.it.galaxytrucker.crewmates.Alien;
import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;

public class CabinModule extends CentralCabin {
    public CabinModule(TileEdge top, TileEdge right, TileEdge bottom, TileEdge left) {
        super(top, right, bottom, left);
    }

    /*
    *   Adds a new {@code Alien} object to the {@code List<Crewmate>} of the cabin.
    *
    *   @throws InvalidActionException if the cabin is already occupied.
    */
    public void addCrewmate (Alien alien) throws InvalidActionException {
        if (!crewmates.isEmpty())
            throw new InvalidActionException("Cabin must be empty for alien");
        this.crewmates.add(alien);
    }
}
