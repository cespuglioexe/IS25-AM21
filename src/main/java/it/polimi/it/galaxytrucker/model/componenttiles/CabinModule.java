package it.polimi.it.galaxytrucker.model.componenttiles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.it.galaxytrucker.model.crewmates.Alien;
import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;

import java.util.List;

/**
 * Represents a Cabin Module that extends the central cabin functionality.
 * This module can house both humans and aliens.
 * <p>
 * A cabin module maintains the ability to house crewmates, but adds the
 * possibility to house a single alien instead of two humans.
 *
 * @author Giacomo Amaducci
 * @version 1.1
 */
public class CabinModule extends CentralCabin {

    /**
     * Constructs a new {@code CabinModule} with the specified edges.
     *
     * @param edges A {@code List} of {@code TileEdge} defining the edges of the cabin module.
     */
    @JsonCreator
    public CabinModule(@JsonProperty("edges") List<TileEdge> edges,
                       @JsonProperty("graphic") String graphicPath) {
        super(edges,graphicPath);
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
