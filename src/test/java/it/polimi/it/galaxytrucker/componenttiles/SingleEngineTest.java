package it.polimi.it.galaxytrucker.componenttiles;

import it.polimi.it.galaxytrucker.model.componenttiles.SingleEngine;
import it.polimi.it.galaxytrucker.model.componenttiles.TileEdge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the SingleEngine component tile class.
 */
public class SingleEngineTest {

    private SingleEngine singleEngine;

    @BeforeEach
    public void setUp() {
        // Create a single engine with different edge types
        singleEngine = new SingleEngine(List.of(TileEdge.SMOOTH, TileEdge.SINGLE, TileEdge.DOUBLE, TileEdge.UNIVERSAL));
    }

    @Test
    public void testFirePowerAfterRotation() {
        // Test that firepower changes is 0.5 when rotated
        singleEngine.rotate();
        assertEquals(1, singleEngine.getRotation());
        assertEquals(0, singleEngine.getEnginePower());

        singleEngine.rotate();
        assertEquals(2, singleEngine.getRotation());
        assertEquals(0, singleEngine.getEnginePower());

        singleEngine.rotate();
        assertEquals(3, singleEngine.getRotation());
        assertEquals(0, singleEngine.getEnginePower());

        // Test that firepower is back to 1 after full rotation
        singleEngine.rotate();
        assertEquals(0, singleEngine.getRotation());
        assertEquals(1, singleEngine.getEnginePower());
    }

}