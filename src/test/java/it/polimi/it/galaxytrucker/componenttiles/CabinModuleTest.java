package it.polimi.it.galaxytrucker.componenttiles;

import it.polimi.it.galaxytrucker.crewmates.Human;
import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CabinModuleTest {

    @Test
    void testAddHuman() {
        CabinModule cabin = new CabinModule(TileEdge.SMOOTH, TileEdge.SMOOTH, TileEdge.SMOOTH, TileEdge.SMOOTH);
        Human human = new Human(false);

        Exception exception = assertThrows(InvalidActionException.class, () -> {
            // Code that should throw the exception
            cabin.addCrewmate(human);
        });

        // Optionally verify the exception message
        assertEquals("Cabin must be empty for alien", exception.getMessage());
    }
}