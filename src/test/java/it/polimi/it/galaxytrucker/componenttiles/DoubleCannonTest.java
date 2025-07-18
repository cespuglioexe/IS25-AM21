package it.polimi.it.galaxytrucker.componenttiles;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.it.galaxytrucker.model.componenttiles.DoubleCannon;
import it.polimi.it.galaxytrucker.model.componenttiles.TileEdge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class DoubleCannonTest {

    private DoubleCannon doubleCannon;

    @BeforeEach
    void setUp() {
        doubleCannon = new DoubleCannon(List.of(TileEdge.SMOOTH, TileEdge.SINGLE, TileEdge.DOUBLE, TileEdge.UNIVERSAL), null);
    }

    @Test
    void testActivate() {
        // Test activation of the DoubleCannon
        assertEquals(2, doubleCannon.activate());
    }

    @Test
    void testGetFirePowerForRotation0() {
        // Test firepower when the rotation is 0
        doubleCannon.activate();
        assertEquals(2, doubleCannon.getFirePower());
    }

    @Test
    void testGetFirePowerForNonZeroRotation() {
        // Test firepower when the rotation is not 0
        doubleCannon.activate();
        doubleCannon.rotate(); // Rotate to non-zero position
        assertEquals(1, doubleCannon.getFirePower());
    }
}