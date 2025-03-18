package it.polimi.it.galaxytrucker.componenttiles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the SingleCannon component tile class.
 */
public class SingleCannonTest {

    private SingleCannon singleCannon;

    @BeforeEach
    public void setUp() {
        // Create a single cannon with different edge types
        singleCannon = new SingleCannon(List.of(TileEdge.SMOOTH, TileEdge.SINGLE, TileEdge.DOUBLE, TileEdge.UNIVERSAL));
    }

    @Test
    public void testInitialFirePower() {
        // Verify that a single cannon has full firepower (1.0) when in initial position (rotation 0)
        assertEquals(0, singleCannon.getRotation());
        assertEquals(1.0, singleCannon.getFirePower());
    }

    @Test
    public void testFirePowerAfterRotation() {
        // Test that firepower changes is 0.5 when rotated
        singleCannon.rotate();
        assertEquals(1, singleCannon.getRotation());
        assertEquals(0.5, singleCannon.getFirePower());

        singleCannon.rotate();
        assertEquals(2, singleCannon.getRotation());
        assertEquals(0.5, singleCannon.getFirePower());

        singleCannon.rotate();
        assertEquals(3, singleCannon.getRotation());
        assertEquals(0.5, singleCannon.getFirePower());

        // Test that firepower is back to 1 after full rotation
        singleCannon.rotate();
        assertEquals(0, singleCannon.getRotation());
        assertEquals(1, singleCannon.getFirePower());
    }
}