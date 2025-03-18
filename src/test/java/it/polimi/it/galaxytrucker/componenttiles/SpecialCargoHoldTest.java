package it.polimi.it.galaxytrucker.componenttiles;

import it.polimi.it.galaxytrucker.utility.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import it.polimi.it.galaxytrucker.utility.Cargo;
import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SpecialCargoHoldTest {

    private SpecialCargoHold specialCargoHold;
    private final int CONTAINER_CAPACITY = 3;

    @BeforeEach
    public void setup() {
        // Create a special cargo hold with 3 container capacity
        specialCargoHold = new SpecialCargoHold(CONTAINER_CAPACITY, List.of(TileEdge.SMOOTH,
                TileEdge.SMOOTH, TileEdge.SMOOTH, TileEdge.SMOOTH));
    }

    @Test
    public void testInitialState() {
        // Verify the special cargo hold is created with the correct properties
        assertEquals(CONTAINER_CAPACITY, specialCargoHold.getContainerNumber());
        assertTrue(specialCargoHold.canHoldSpecialCargo());
        assertTrue(specialCargoHold.getContainedCargo().isEmpty());
    }

    @Test
    public void testAddCargo() {
        Cargo regularCargo = new Cargo(Color.BLUE);
        Cargo specialCargo = new Cargo(Color.RED);

        // Add both types of cargo
        specialCargoHold.addCargo(regularCargo);
        specialCargoHold.addCargo(specialCargo);

        // Verify both types of cargo were added
        assertEquals(2, specialCargoHold.getContainedCargo().size());
        assertEquals(regularCargo, specialCargoHold.getContainedCargo().get(0));
        assertEquals(specialCargo, specialCargoHold.getContainedCargo().get(1));
    }

    @Test
    public void testCapacityLimits() throws InvalidActionException {
        // Fill the cargo hold to capacity
        for (int i = 0; i < CONTAINER_CAPACITY; i++) {
            specialCargoHold.addCargo(new Cargo(Color.values()[i % 4])); // Mix of regular and special
        }

        // Verify it's at capacity
        assertEquals(CONTAINER_CAPACITY, specialCargoHold.getContainedCargo().size());

        // Try to add one more should throw exception
        InvalidActionException exception = assertThrows(
                InvalidActionException.class,
                () -> specialCargoHold.addCargo(new Cargo(Color.BLUE))
        );

        assertEquals("CargoHold is already full, can't add cargo", exception.getMessage());
    }

    @Test
    public void testRemovingCargo() throws InvalidActionException {
        // Add a mix of cargo types
        Cargo regularCargo = new Cargo(Color.YELLOW);
        Cargo specialCargo = new Cargo(Color.RED);

        specialCargoHold.addCargo(regularCargo);
        specialCargoHold.addCargo(specialCargo);

        // Remove cargo and verify
        Cargo removed = specialCargoHold.removeCargo(0);
        assertEquals(regularCargo, removed);
        assertEquals(1, specialCargoHold.getContainedCargo().size());
        assertEquals(specialCargo, specialCargoHold.getContainedCargo().get(0));
    }

    @Test
    public void testWithDifferentCapacities() {
        // Test with different container capacities
        SpecialCargoHold smallHold = new SpecialCargoHold(1, List.of(TileEdge.SMOOTH,
                TileEdge.SMOOTH, TileEdge.SMOOTH, TileEdge.SMOOTH));
        SpecialCargoHold largeHold = new SpecialCargoHold(5, List.of(TileEdge.SMOOTH,
                TileEdge.SMOOTH, TileEdge.SMOOTH, TileEdge.SMOOTH));

        assertEquals(1, smallHold.getContainerNumber());
        assertEquals(5, largeHold.getContainerNumber());
        assertTrue(smallHold.canHoldSpecialCargo());
        assertTrue(largeHold.canHoldSpecialCargo());
    }

    @Test
    public void testComparingWithRegularCargoHold() {
        // Create a regular cargo hold with the same capacity
        CargoHold regularHold = new CargoHold(CONTAINER_CAPACITY, List.of(TileEdge.SMOOTH,
                TileEdge.SMOOTH, TileEdge.SMOOTH, TileEdge.SMOOTH));

        // Regular hold should not accept special cargo
        assertFalse(regularHold.canHoldSpecialCargo());

        // Special hold should accept special cargo
        assertTrue(specialCargoHold.canHoldSpecialCargo());

        // Both should have the same container capacity
        assertEquals(regularHold.getContainerNumber(), specialCargoHold.getContainerNumber());
    }
}