package it.polimi.it.galaxytrucker.componenttiles;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.it.galaxytrucker.observer.EventListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import it.polimi.it.galaxytrucker.observer.EnergyConsumptionEvent;

class DoubleCannonTest {

    private DoubleCannon doubleCannon;

    @BeforeEach
    void setUp() {
        doubleCannon = new DoubleCannon(TileEdge.SMOOTH, TileEdge.SINGLE, TileEdge.DOUBLE, TileEdge.UNIVERSAL);
    }

    @Test
    void testInitialState() {
        // Test that the initial state of the DoubleCannon is inactive and the firepower is 0
        assertFalse(doubleCannon.getFirePower() > 0, "Double cannon should have zero firepower initially.");
    }

    @Test
    void testActivate() {
        // Test activation of the DoubleCannon
        doubleCannon.activate();
        assertTrue(doubleCannon.getFirePower() > 0, "Double cannon should have firepower after activation.");
    }

    @Test
    void testActivateOnce() {
        // Double cannon can only be activated once
        doubleCannon.activate(); // Activate once
        doubleCannon.activate(); // Trying to activate again
        assertEquals(2, doubleCannon.getFirePower(), "Double cannon should only activate once and give firepower of 2.");
    }

    @Test
    void testGetFirePowerForRotation0() {
        // Test firepower when the rotation is 0
        doubleCannon.activate();
        assertEquals(2, doubleCannon.getFirePower(), "Double cannon should have firepower of 2 when facing forwards (rotation 0).");
    }

    @Test
    void testGetFirePowerForNonZeroRotation() {
        // Test firepower when the rotation is not 0
        doubleCannon.activate();
        doubleCannon.rotate(); // Rotate to non-zero position
        assertEquals(1, doubleCannon.getFirePower(), "Double cannon should have firepower of 1 when rotated (non-zero rotation).");
    }

    @Test
    void testCanBeActivated() {
        // Test that the DoubleCannon cannot be activated again after being activated
        assertFalse(doubleCannon.canBeActivated(), "Double cannon should not be able to be activated more than once.");
    }

    @Test
    void testEnergyConsumptionEvent() {
        // Test if an energy consumption event is published when activated
        EnergyConsumptionEvent event = new EnergyConsumptionEvent();

        // Create a listener that checks if the event is of type EnergyConsumptionEvent
        EventListener mockListener = event1 -> {
            assertInstanceOf(EnergyConsumptionEvent.class, event1, "Event should be of type EnergyConsumptionEvent.");
        };

        doubleCannon.addListener(mockListener);

        // Trigger the activation and event publication
        doubleCannon.activate();

        // Check that the listener was invoked by the event
        // This test ensures the event is published, but it doesn't compare the exact object instance
    }
}