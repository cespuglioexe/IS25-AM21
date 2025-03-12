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
        doubleEngine = new DoubleEngine(TileEdge.SMOOTH, TileEdge.SINGLE, TileEdge.DOUBLE, TileEdge.UNIVERSAL);
    }

    @Test
    public void testEngineConstruction() {
        // Test that the engine is constructed with the correct edges
        List<TileEdge> edges = doubleEngine.getTileEdges();
        assertEquals(4, edges.size(), "Should have 4 edges");
        assertEquals(TileEdge.SMOOTH, edges.get(0), "Top edge should be SMOOTH");
        assertEquals(TileEdge.SINGLE, edges.get(1), "Right edge should be SINGLE");
        assertEquals(TileEdge.DOUBLE, edges.get(2), "Bottom edge should be DOUBLE");
        assertEquals(TileEdge.UNIVERSAL, edges.get(3), "Left edge should be UNIVERSAL");
    }

    @Test
    public void testInitialState() {
        // Verify initial state - engine should be inactive and return 0 power
        assertEquals(0, doubleEngine.getEnginePower(), "Initial engine power should be 0 when inactive");
    }

    @Test
    public void testActivation() {
        // Test activation process
        // Register a mock event listener to capture the energy consumption event
        AtomicBoolean eventReceived = new AtomicBoolean(false);
        EventListener mockListener = event -> {
            if (event instanceof EnergyConsumptionEvent) {
                eventReceived.set(true);
            }
        };

        doubleEngine.addListener(mockListener);

        // Activate the engine
        doubleEngine.activate();

        // Verify event was published
        assertTrue(eventReceived.get(), "Energy consumption event should be published upon activation");

        // Verify engine power after activation
        assertEquals(2, doubleEngine.getEnginePower(), "Engine power should be 2 after activation");

        // After getting engine power, engine should deactivate automatically
        assertEquals(0, doubleEngine.getEnginePower(), "Engine power should return to 0 after use");
    }

    @Test
    public void testReactivation() {
        // Test that the engine can't be reactivated
        assertFalse(doubleEngine.canBeActivated(), "Double engine should report that it cannot be activated");

        // Activate once
        doubleEngine.activate();
        assertEquals(2, doubleEngine.getEnginePower(), "Engine power should be 2 after first activation");
        assertEquals(0, doubleEngine.getEnginePower(), "Engine power should return to 0 after use");

        // Try to activate again and check if a new event is published
        AtomicBoolean secondEventReceived = new AtomicBoolean(false);
        EventListener mockListener = event -> {
            if (event instanceof EnergyConsumptionEvent) {
                secondEventReceived.set(true);
            }
        };

        doubleEngine.addListener(mockListener);
        doubleEngine.activate();

        // Even though canBeActivated() returns false, the implementation allows reactivation
        assertTrue(secondEventReceived.get(), "Energy consumption event should be published upon reactivation");
        assertEquals(2, doubleEngine.getEnginePower(), "Engine power should be 2 after reactivation");
    }

    @Test
    public void testInheritedRotationFunctionality() {
        // Test that the inherited rotation functionality works correctly

        // Initial state
        assertEquals(0, doubleEngine.getRotation(), "Initial rotation should be 0");

        // After one rotation
        doubleEngine.rotate();
        assertEquals(1, doubleEngine.getRotation(), "Rotation should be 1 after one rotation");

        List<TileEdge> rotatedEdges = doubleEngine.getTileEdges();
        assertEquals(TileEdge.UNIVERSAL, rotatedEdges.get(0), "Top edge should be UNIVERSAL after one rotation");
        assertEquals(TileEdge.SMOOTH, rotatedEdges.get(1), "Right edge should be SMOOTH after one rotation");
        assertEquals(TileEdge.SINGLE, rotatedEdges.get(2), "Bottom edge should be SINGLE after one rotation");
        assertEquals(TileEdge.DOUBLE, rotatedEdges.get(3), "Left edge should be DOUBLE after one rotation");

        // Verify that activation and power are independent of rotation
        doubleEngine.activate();
        assertEquals(2, doubleEngine.getEnginePower(), "Engine power should be 2 after activation regardless of rotation");
    }

    @Test
    public void testMultipleActivations() {
        // Test behavior with multiple activations

        // First activation
        doubleEngine.activate();
        assertEquals(2, doubleEngine.getEnginePower(), "Engine power should be 2 after first activation");
        assertEquals(0, doubleEngine.getEnginePower(), "Engine power should be 0 after getting power once");

        // Second activation
        doubleEngine.activate();
        assertEquals(2, doubleEngine.getEnginePower(), "Engine power should be 2 after second activation");
        assertEquals(0, doubleEngine.getEnginePower(), "Engine power should be 0 after getting power again");

        // Third activation
        doubleEngine.activate();
        assertEquals(2, doubleEngine.getEnginePower(), "Engine power should be 2 after third activation");
        assertEquals(0, doubleEngine.getEnginePower(), "Engine power should be 0 after getting power a third time");
    }

    @Test
    public void testActivateMultipleTimesBeforeGetPower() {
        // Test behavior when activating multiple times before calling getPower

        // Activate multiple times
        doubleEngine.activate();
        doubleEngine.activate();
        doubleEngine.activate();

        // Should still return power of 2 once, then 0
        assertEquals(2, doubleEngine.getEnginePower(), "Engine power should be 2 after multiple activations");
        assertEquals(0, doubleEngine.getEnginePower(), "Engine power should be 0 after getting power once");
    }

    @Test
    public void testDifferentEdgeConfigurations() {
        // Test with all universal edges
        DoubleEngine universalEngine = new DoubleEngine(
                TileEdge.UNIVERSAL, TileEdge.UNIVERSAL, TileEdge.UNIVERSAL, TileEdge.UNIVERSAL);

        universalEngine.activate();
        assertEquals(2, universalEngine.getEnginePower(), "Engine power should be 2 after activation regardless of edge configuration");

        // Test with all incompatible edges
        DoubleEngine incompatibleEngine = new DoubleEngine(
                TileEdge.INCOMPATIBLE, TileEdge.INCOMPATIBLE, TileEdge.INCOMPATIBLE, TileEdge.INCOMPATIBLE);

        incompatibleEngine.activate();
        assertEquals(2, incompatibleEngine.getEnginePower(), "Engine power should be 2 after activation regardless of edge configuration");
    }

    @Test
    public void testEnergyConsumerImplementation() {
        // Test that DoubleEngine correctly implements the EnergyConsumer interface
        assertInstanceOf(EnergyConsumer.class, doubleEngine, "DoubleEngine should implement EnergyConsumer interface");

        // Test canBeActivated method
        assertFalse(doubleEngine.canBeActivated(), "Double engine should report that it cannot be activated");

        // Test that the activate method works as expected for an EnergyConsumer
        AtomicBoolean eventReceived = new AtomicBoolean(false);
        EventListener mockListener = event -> {
            if (event instanceof EnergyConsumptionEvent) {
                eventReceived.set(true);
            }
        };

        doubleEngine.addListener(mockListener);

        // Call activate through the EnergyConsumer interface
        EnergyConsumer consumer = doubleEngine;
        consumer.activate();

        assertTrue(eventReceived.get(), "Energy consumption event should be published when activating via EnergyConsumer interface");
        assertEquals(2, doubleEngine.getEnginePower(), "Engine power should be 2 after activation via EnergyConsumer interface");
    }

    @Test
    public void testEventPublishing() {
        // Test that the event is only published once per activation
        int[] eventCount = {0};
        EventListener countingListener = event -> {
            if (event instanceof EnergyConsumptionEvent) {
                eventCount[0]++;
            }
        };

        doubleEngine.addListener(countingListener);

        // First activation
        doubleEngine.activate();
        assertEquals(1, eventCount[0], "One event should be published after first activation");

        // Get power (which deactivates the engine)
        doubleEngine.getEnginePower();
        assertEquals(1, eventCount[0], "No additional events should be published when getting power");

        // Second activation
        doubleEngine.activate();
        assertEquals(2, eventCount[0], "One event should be published after second activation");

        // Multiple activations without getting power
        doubleEngine.getEnginePower(); // Deactivate first
        doubleEngine.activate();
        doubleEngine.activate();
        assertEquals(3, eventCount[0], "Only one event should be published per activation transition");
    }
}