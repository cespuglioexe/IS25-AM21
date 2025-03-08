package it.polimi.it.galaxytrucker.componenttiles;

import it.polimi.it.galaxytrucker.utility.Direction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShieldTest {

    @Test
    void getOrientation() {
        Shield shield = new Shield(Direction.UP, TileEdge.SMOOTH, TileEdge.SMOOTH, TileEdge.SMOOTH, TileEdge.SMOOTH);

        assertEquals(Direction.UP, shield.getOrientation());

        shield.rotate();

        assertEquals(Direction.RIGHT, shield.getOrientation());

        shield.rotate();

        assertEquals(Direction.DOWN, shield.getOrientation());

        shield.rotate();

        assertEquals(Direction.LEFT, shield.getOrientation());

        shield.rotate();

        assertEquals(Direction.UP, shield.getOrientation());
    }
}