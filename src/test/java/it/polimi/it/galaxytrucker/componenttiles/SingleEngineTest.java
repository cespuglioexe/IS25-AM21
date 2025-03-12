package it.polimi.it.galaxytrucker.componenttiles;

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
        singleEngine = new SingleEngine(TileEdge.SMOOTH, TileEdge.SINGLE, TileEdge.DOUBLE, TileEdge.UNIVERSAL);
    }

    @Test
    public void testEngineConstruction() {
        // Test that the engine is constructed with the correct edges
        List<TileEdge> edges = singleEngine.getTileEdges();
        assertEquals(4, edges.size(), "Should have 4 edges");
        assertEquals(TileEdge.SMOOTH, edges.get(0), "Top edge should be SMOOTH");
        assertEquals(TileEdge.SINGLE, edges.get(1), "Right edge should be SINGLE");
        assertEquals(TileEdge.DOUBLE, edges.get(2), "Bottom edge should be DOUBLE");
        assertEquals(TileEdge.UNIVERSAL, edges.get(3), "Left edge should be UNIVERSAL");
    }

    @Test
    public void testEnginePower() {
        // Verify that a single engine always has power value of 1
        assertEquals(1, singleEngine.getEnginePower(), "Single engine should have power of 1");
    }

    @Test
    public void testInheritedRotationFunctionality() {
        // Test that the inherited rotation functionality works correctly

        // Initial state
        assertEquals(0, singleEngine.getRotation(), "Initial rotation should be 0");

        // After one rotation
        singleEngine.rotate();
        assertEquals(1, singleEngine.getRotation(), "Rotation should be 1 after one rotation");

        List<TileEdge> rotatedEdges = singleEngine.getTileEdges();
        assertEquals(TileEdge.UNIVERSAL, rotatedEdges.get(0), "Top edge should be UNIVERSAL after one rotation");
        assertEquals(TileEdge.SMOOTH, rotatedEdges.get(1), "Right edge should be SMOOTH after one rotation");
        assertEquals(TileEdge.SINGLE, rotatedEdges.get(2), "Bottom edge should be SINGLE after one rotation");
        assertEquals(TileEdge.DOUBLE, rotatedEdges.get(3), "Left edge should be DOUBLE after one rotation");

        // Verify that engine power remains constant after rotation
        assertEquals(1, singleEngine.getEnginePower(), "Engine power should remain 1 after rotation");
    }

    @Test
    public void testInheritedFullRotation() {
        // Rotate four times to complete a full circle
        for (int i = 0; i < 4; i++) {
            singleEngine.rotate();
        }

        // Test rotation value (should be back to 0)
        assertEquals(0, singleEngine.getRotation(), "Rotation should be 0 after four rotations (full circle)");

        // Test edge order after four rotations (should be back to original)
        List<TileEdge> edges = singleEngine.getTileEdges();
        assertEquals(TileEdge.SMOOTH, edges.get(0), "Top edge should be SMOOTH after full rotation");
        assertEquals(TileEdge.SINGLE, edges.get(1), "Right edge should be SINGLE after full rotation");
        assertEquals(TileEdge.DOUBLE, edges.get(2), "Bottom edge should be DOUBLE after full rotation");
        assertEquals(TileEdge.UNIVERSAL, edges.get(3), "Left edge should be UNIVERSAL after full rotation");

        // Verify that engine power remains constant after full rotation
        assertEquals(1, singleEngine.getEnginePower(), "Engine power should remain 1 after full rotation");
    }

    @Test
    public void testDifferentEdgeConfigurations() {
        // Test with all universal edges
        SingleEngine universalEngine = new SingleEngine(
                TileEdge.UNIVERSAL, TileEdge.UNIVERSAL, TileEdge.UNIVERSAL, TileEdge.UNIVERSAL);

        assertEquals(1, universalEngine.getEnginePower(), "Engine power should be 1 regardless of edge configuration");

        // Test with all incompatible edges
        SingleEngine incompatibleEngine = new SingleEngine(
                TileEdge.INCOMPATIBLE, TileEdge.INCOMPATIBLE, TileEdge.INCOMPATIBLE, TileEdge.INCOMPATIBLE);

        assertEquals(1, incompatibleEngine.getEnginePower(), "Engine power should be 1 regardless of edge configuration");

        // Verify edges were set correctly
        List<TileEdge> edges = incompatibleEngine.getTileEdges();
        for (TileEdge edge : edges) {
            assertEquals(TileEdge.INCOMPATIBLE, edge, "All edges should be INCOMPATIBLE");
        }
    }

    @Test
    public void testMixedEdgeConfigurations() {
        // Test with a mixed configuration of edges
        SingleEngine mixedEngine = new SingleEngine(
                TileEdge.SMOOTH, TileEdge.INCOMPATIBLE, TileEdge.DOUBLE, TileEdge.SINGLE);

        List<TileEdge> edges = mixedEngine.getTileEdges();
        assertEquals(TileEdge.SMOOTH, edges.get(0), "Top edge should be SMOOTH");
        assertEquals(TileEdge.INCOMPATIBLE, edges.get(1), "Right edge should be INCOMPATIBLE");
        assertEquals(TileEdge.DOUBLE, edges.get(2), "Bottom edge should be DOUBLE");
        assertEquals(TileEdge.SINGLE, edges.get(3), "Left edge should be SINGLE");

        assertEquals(1, mixedEngine.getEnginePower(), "Engine power should be 1 regardless of edge configuration");
    }
}