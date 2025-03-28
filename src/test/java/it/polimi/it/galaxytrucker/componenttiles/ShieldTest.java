package it.polimi.it.galaxytrucker.componenttiles;

import it.polimi.it.galaxytrucker.utility.Direction;
import jdk.jfr.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ShieldTest {

    private Shield shield;

    @BeforeEach
    public void setup() {
        shield = new Shield(Direction.UP, List.of(TileEdge.SMOOTH, TileEdge.SMOOTH, TileEdge.SMOOTH, TileEdge.SMOOTH));
    }

    @Test
    public void testOrientationWithRotation() {
        // Test orientation with no rotation (default)
        assertEquals(Direction.UP, shield.getOrientation().get(0));
        assertEquals(Direction.RIGHT, shield.getOrientation().get(1));

        // Test with 1 rotation (90 degrees clockwise)
        shield.rotate();
        assertEquals(Direction.RIGHT, shield.getOrientation().get(0));
        assertEquals(Direction.DOWN, shield.getOrientation().get(1));

        // Test with 2 rotations (180 degrees)
        shield.rotate();
        assertEquals(Direction.DOWN, shield.getOrientation().get(0));
        assertEquals(Direction.LEFT, shield.getOrientation().get(1));

        // Test with 3 rotations (270 degrees)
        shield.rotate();
        assertEquals(Direction.LEFT, shield.getOrientation().get(0));
        assertEquals(Direction.UP, shield.getOrientation().get(1));

        // Test with 4 rotations (back to original)
        shield.rotate();
        assertEquals(Direction.UP, shield.getOrientation().get(0));
        assertEquals(Direction.RIGHT, shield.getOrientation().get(1));
    }

    @Test
    public void testAllDirectionPairs() {
        // Create shields with different orientations
        Shield upShield = new Shield(Direction.UP, List.of(TileEdge.SMOOTH, TileEdge.SMOOTH,
                TileEdge.SMOOTH, TileEdge.SMOOTH));
        Shield rightShield = new Shield(Direction.RIGHT, List.of(TileEdge.SMOOTH, TileEdge.SMOOTH,
                TileEdge.SMOOTH, TileEdge.SMOOTH));
        Shield downShield = new Shield(Direction.DOWN, List.of(TileEdge.SMOOTH, TileEdge.SMOOTH,
                TileEdge.SMOOTH, TileEdge.SMOOTH));
        Shield leftShield = new Shield(Direction.LEFT, List.of(TileEdge.SMOOTH, TileEdge.SMOOTH,
                TileEdge.SMOOTH, TileEdge.SMOOTH));

        // Verify orientations
        assertEquals(List.of(Direction.UP,      Direction.RIGHT),   upShield.getOrientation());
        assertEquals(List.of(Direction.RIGHT,   Direction.DOWN),    rightShield.getOrientation());
        assertEquals(List.of(Direction.DOWN,    Direction.LEFT),    downShield.getOrientation());
        assertEquals(List.of(Direction.LEFT,    Direction.UP),      leftShield.getOrientation());
    }

    @Test
    public void testMultipleRotations() {
        // Test multiple rotations at once
        for (int i = 0; i < 10; i++) {
            assertEquals(Direction.values()[(Direction.UP.ordinal() + i) % 4], shield.getOrientation().get(0));
            shield.rotate();
        }
    }
}