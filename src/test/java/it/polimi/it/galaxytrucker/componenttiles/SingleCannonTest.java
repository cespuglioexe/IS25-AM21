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
        singleCannon = new SingleCannon(TileEdge.SMOOTH, TileEdge.SINGLE, TileEdge.DOUBLE, TileEdge.UNIVERSAL);
    }

    @Test
    public void testCannonConstruction() {
        // Test that the cannon is constructed with the correct edges
        List<TileEdge> edges = singleCannon.getTileEdges();
        assertEquals(4, edges.size(), "Should have 4 edges");
        assertEquals(TileEdge.SMOOTH, edges.get(0), "Top edge should be SMOOTH");
        assertEquals(TileEdge.SINGLE, edges.get(1), "Right edge should be SINGLE");
        assertEquals(TileEdge.DOUBLE, edges.get(2), "Bottom edge should be DOUBLE");
        assertEquals(TileEdge.UNIVERSAL, edges.get(3), "Left edge should be UNIVERSAL");
    }

    @Test
    public void testInitialFirePower() {
        // Verify that a single cannon has full firepower (1.0) when in initial position (rotation 0)
        assertEquals(0, singleCannon.getRotation(), "Initial rotation should be a 0");
        assertEquals(1.0, singleCannon.getFirePower(), "Single cannon should have firepower of 1.0 when facing forward");
    }

    @Test
    public void testFirePowerAfterRotation() {
        // Test that firepower changes to 0.5 after rotation
        singleCannon.rotate(); // Rotation is now 1
        assertEquals(1, singleCannon.getRotation(), "Rotation should be 1 after one rotation");
        assertEquals(0.5, singleCannon.getFirePower(), "Firepower should be 0.5 when not facing forward");

        singleCannon.rotate(); // Rotation is now 2
        assertEquals(2, singleCannon.getRotation(), "Rotation should be 2 after two rotations");
        assertEquals(0.5, singleCannon.getFirePower(), "Firepower should remain 0.5 after second rotation");

        singleCannon.rotate(); // Rotation is now 3
        assertEquals(3, singleCannon.getRotation(), "Rotation should be 3 after three rotations");
        assertEquals(0.5, singleCannon.getFirePower(), "Firepower should remain 0.5 after third rotation");
    }

    @Test
    public void testFirePowerAfterFullRotation() {
        // Rotate four times to complete a full circle
        for (int i = 0; i < 4; i++) {
            singleCannon.rotate();
        }

        // Test rotation value (should be back to 0)
        assertEquals(0, singleCannon.getRotation(), "Rotation should be 0 after four rotations (full circle)");

        // Firepower should be back to 1.0
        assertEquals(1.0, singleCannon.getFirePower(), "Firepower should be back to 1.0 after full rotation");
    }

    @Test
    public void testFirePowerCycleThroughAllRotations() {
        // Initial state - rotation 0
        assertEquals(0, singleCannon.getRotation(), "Initial rotation should be 0");
        assertEquals(1.0, singleCannon.getFirePower(), "Initial firepower should be 1.0");

        // First rotation - rotation 1
        singleCannon.rotate();
        assertEquals(1, singleCannon.getRotation(), "Rotation should be 1 after one rotation");
        assertEquals(0.5, singleCannon.getFirePower(), "Firepower should be 0.5 after first rotation");

        // Second rotation - rotation 2
        singleCannon.rotate();
        assertEquals(2, singleCannon.getRotation(), "Rotation should be 2 after two rotations");
        assertEquals(0.5, singleCannon.getFirePower(), "Firepower should be 0.5 after second rotation");

        // Third rotation - rotation 3
        singleCannon.rotate();
        assertEquals(3, singleCannon.getRotation(), "Rotation should be 3 after three rotations");
        assertEquals(0.5, singleCannon.getFirePower(), "Firepower should be 0.5 after third rotation");

        // Fourth rotation - back to rotation 0
        singleCannon.rotate();
        assertEquals(0, singleCannon.getRotation(), "Rotation should be 0 after four rotations");
        assertEquals(1.0, singleCannon.getFirePower(), "Firepower should be back to 1.0 after fourth rotation");
    }

    @Test
    public void testInheritedRotationAffectsEdgeOrder() {
        // Initial state
        List<TileEdge> initialEdges = singleCannon.getTileEdges();
        assertEquals(TileEdge.SMOOTH, initialEdges.get(0), "Top edge should be SMOOTH initially");
        assertEquals(TileEdge.SINGLE, initialEdges.get(1), "Right edge should be SINGLE initially");
        assertEquals(TileEdge.DOUBLE, initialEdges.get(2), "Bottom edge should be DOUBLE initially");
        assertEquals(TileEdge.UNIVERSAL, initialEdges.get(3), "Left edge should be UNIVERSAL initially");

        // After one rotation
        singleCannon.rotate();
        List<TileEdge> rotatedEdges = singleCannon.getTileEdges();
        assertEquals(TileEdge.UNIVERSAL, rotatedEdges.get(0), "Top edge should be UNIVERSAL after one rotation");
        assertEquals(TileEdge.SMOOTH, rotatedEdges.get(1), "Right edge should be SMOOTH after one rotation");
        assertEquals(TileEdge.SINGLE, rotatedEdges.get(2), "Bottom edge should be SINGLE after one rotation");
        assertEquals(TileEdge.DOUBLE, rotatedEdges.get(3), "Left edge should be DOUBLE after one rotation");
    }

    @Test
    public void testDifferentEdgeConfigurations() {
        // Test with all universal edges
        SingleCannon universalCannon = new SingleCannon(
                TileEdge.UNIVERSAL, TileEdge.UNIVERSAL, TileEdge.UNIVERSAL, TileEdge.UNIVERSAL);

        assertEquals(1.0, universalCannon.getFirePower(), "Firepower should be 1.0 in initial position regardless of edge configuration");

        universalCannon.rotate();
        assertEquals(0.5, universalCannon.getFirePower(), "Firepower should be 0.5 after rotation regardless of edge configuration");

        // Test with all incompatible edges
        SingleCannon incompatibleCannon = new SingleCannon(
                TileEdge.INCOMPATIBLE, TileEdge.INCOMPATIBLE, TileEdge.INCOMPATIBLE, TileEdge.INCOMPATIBLE);

        assertEquals(1.0, incompatibleCannon.getFirePower(), "Firepower should be 1.0 in initial position regardless of edge configuration");

        incompatibleCannon.rotate();
        assertEquals(0.5, incompatibleCannon.getFirePower(), "Firepower should be 0.5 after rotation regardless of edge configuration");
    }

    @Test
    public void testMultipleRotationsAndResets() {
        // Verify behavior after multiple rotation cycles

        // First cycle
        for (int i = 0; i < 4; i++) {
            singleCannon.rotate();
        }
        assertEquals(0, singleCannon.getRotation(), "Rotation should be 0 after one complete cycle");
        assertEquals(1.0, singleCannon.getFirePower(), "Firepower should be 1.0 after one complete cycle");

        // Second cycle
        for (int i = 0; i < 4; i++) {
            singleCannon.rotate();
        }
        assertEquals(0, singleCannon.getRotation(), "Rotation should be 0 after two complete cycles");
        assertEquals(1.0, singleCannon.getFirePower(), "Firepower should be 1.0 after two complete cycles");

        // Partial third cycle (2 rotations)
        singleCannon.rotate();
        singleCannon.rotate();
        assertEquals(2, singleCannon.getRotation(), "Rotation should be 2 after two additional rotations");
        assertEquals(0.5, singleCannon.getFirePower(), "Firepower should be 0.5 after two additional rotations");
    }
}