package it.polimi.it.galaxytrucker.componenttiles;

import it.polimi.it.galaxytrucker.observer.EnergyConsumptionEvent;
import it.polimi.it.galaxytrucker.observer.EventListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the DoubleEngine component tile class.
 */
public class DoubleEngineTest {

    private DoubleEngine doubleEngine;

    @BeforeEach
    public void setUp() {
        // Create a double engine with different edge types
        doubleEngine = new DoubleEngine(List.of(TileEdge.SMOOTH, TileEdge.SINGLE, TileEdge.DOUBLE, TileEdge.UNIVERSAL));
    }

    @Test
    void testActivate() {
        // Test activation of the DoubleCannon
        assertEquals(2, doubleEngine.activate());
    }

    @Test
    void testGetEnginePowerForNonZeroRotation() {
        // Test firepower when the rotation is not 0
        doubleEngine.rotate(); // Rotate to non-zero position
        assertEquals(0, doubleEngine.getEnginePower());
    }
}