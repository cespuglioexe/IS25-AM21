package it.polimi.it.galaxytrucker.componenttiles;

import it.polimi.it.galaxytrucker.crewmates.Alien;
import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;

/**
 * Represents a cabin module that extends the central cabin functionality.
 * This module can house a single alien crewmate at a time.
 * <p>
 * A cabin module is defined by its four edges (top, right, bottom, left)
 * and maintains the ability to house crewmates with the restriction that
 * only one crewmate can occupy the cabin at any time.
 *
 * @author Giacomo Amaducci
 * @version 1.0
 */
public class CabinModule extends CentralCabin {

    /**
     * Constructs a new CabinModule with the specified edges.
     *
     * @param top    The edge type for the top side of the cabin
     * @param right  The edge type for the right side of the cabin
     * @param bottom The edge type for the bottom side of the cabin
     * @param left   The edge type for the left side of the cabin
     */
    public CabinModule(TileEdge top, TileEdge right, TileEdge bottom, TileEdge left) {
        super(top, right, bottom, left);
    }

    /**
     * Adds an alien crewmate to this cabin module.
     * <p>
     * This method enforces the rule that a cabin module can only house a single
     * crewmate if it's of type {@code Alien}. Attempting to add an alien to an
     * occupied cabin will result in an {@code InvalidActionException}.
     *
     * @param alien The alien crewmate to be added to the cabin
     * @throws InvalidActionException If the cabin is already occupied by another crewmate
     */
    public void addCrewmate(Alien alien) throws InvalidActionException {
        if (!crewmates.isEmpty())
            throw new InvalidActionException("Cabin must be empty for alien");
        this.crewmates.add(alien);
    }
}