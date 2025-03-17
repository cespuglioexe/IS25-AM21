package it.polimi.it.galaxytrucker.componenttiles;

import it.polimi.it.galaxytrucker.observer.EnergyConsumptionEvent;
import it.polimi.it.galaxytrucker.observer.EventListener;
import it.polimi.it.galaxytrucker.utility.Direction;
import jdk.jfr.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ShieldTest {

    private Shield shield;
    private TestObserver observer;

    // Inner class for testing the observer pattern
    private static class TestObserver implements EventListener {
        private boolean notified = false;

        public boolean wasNotified() {
            return notified;
        }

        public void reset() {
            notified = false;
        }

        @Override
        public void notify(Event event) {
            notified = true;
        }
    }

    @BeforeEach
    public void setup() {
        shield = new Shield(Direction.UP, TileEdge.SMOOTH, TileEdge.SMOOTH,
                TileEdge.SMOOTH, TileEdge.SMOOTH);
        observer = new TestObserver();
        shield.addListener(observer);
    }

    @Test
    public void testInitialState() {
        // Verify the shield is created with the correct orientation and is inactive
        assertEquals(Direction.UP, shield.getOrientation());
        assertFalse(observer.wasNotified()); // Shield shouldn't publish on creation
    }

    @Test
    public void testActivation() {
        // Activate the shield
        shield.activate();

        // Verify the observer was notified of energy consumption
        assertTrue(observer.wasNotified());

        // Reset the observer
        observer.reset();

        // Activate again should not notify again (shield is already active)
        shield.activate();
        assertFalse(observer.wasNotified());
    }

    @Test
    public void testCanBeActivated() {
        // Verify canBeActivated always returns false
        assertFalse(shield.canBeActivated());

        // Even after activation
        shield.activate();
        assertFalse(shield.canBeActivated());
    }

    @Test
    public void testOrientationWithRotation() {
        // Test orientation with no rotation (default)
        assertEquals(Direction.UP, shield.getOrientation());

        // Test with 1 rotation (90 degrees clockwise)
        shield.rotate();
        assertEquals(Direction.RIGHT, shield.getOrientation());

        // Test with 2 rotations (180 degrees)
        shield.rotate();
        assertEquals(Direction.DOWN, shield.getOrientation());

        // Test with 3 rotations (270 degrees)
        shield.rotate();
        assertEquals(Direction.LEFT, shield.getOrientation());

        // Test with 4 rotations (back to original)
        shield.rotate();
        assertEquals(Direction.UP, shield.getOrientation());
    }

    @Test
    public void testAllDirectionPairs() {
        // Create shields with different orientations
        Shield upShield = new Shield(Direction.UP, TileEdge.SMOOTH, TileEdge.SMOOTH,
                TileEdge.SMOOTH, TileEdge.SMOOTH);
        Shield rightShield = new Shield(Direction.RIGHT, TileEdge.SMOOTH, TileEdge.SMOOTH,
                TileEdge.SMOOTH, TileEdge.SMOOTH);
        Shield downShield = new Shield(Direction.DOWN, TileEdge.SMOOTH, TileEdge.SMOOTH,
                TileEdge.SMOOTH, TileEdge.SMOOTH);
        Shield leftShield = new Shield(Direction.LEFT, TileEdge.SMOOTH, TileEdge.SMOOTH,
                TileEdge.SMOOTH, TileEdge.SMOOTH);

        // Verify orientations
        assertEquals(List.of(Direction.UP, Direction.values()[(Direction.UP.ordinal() + 1) % 4]), upShield.getOrientation());
        assertEquals(List.of(Direction.RIGHT, Direction.values()[(Direction.RIGHT.ordinal() + 1) % 4]), rightShield.getOrientation());
        assertEquals(List.of(Direction.DOWN, Direction.values()[(Direction.DOWN.ordinal() + 1) % 4]), downShield.getOrientation());
        assertEquals(List.of(Direction.LEFT, Direction.values()[(Direction.LEFT.ordinal() + 1) % 4]), leftShield.getOrientation());
    }

    @Test
    public void testMultipleRotations() {
        // Test multiple rotations at once
        for (int i = 0; i < 10; i++) {
            shield.rotate();
            assertEquals(Direction.values()[(Direction.UP.ordinal() + (i + 1) % 4) % 4], shield.getOrientation());
        }
    }

    @Test
    public void testObserverPattern() {
        // Create a second observer
        TestObserver secondObserver = new TestObserver();
        shield.addListener(secondObserver);

        // Activate and verify both observers were notified
        shield.activate();
        assertTrue(observer.wasNotified());
        assertTrue(secondObserver.wasNotified());

        // Unsubscribe first observer
        shield.removeListener(observer);

        // Reset both observers
        observer.reset();
        secondObserver.reset();

        // Create a new shield and activate
        Shield newShield = new Shield(Direction.UP, TileEdge.SMOOTH, TileEdge.SMOOTH,
                TileEdge.SMOOTH, TileEdge.SMOOTH);
        newShield.addListener(secondObserver);
        newShield.activate();

        // First observer shouldn't be notified, but second one should
        assertFalse(observer.wasNotified());
        assertTrue(secondObserver.wasNotified());
    }
}