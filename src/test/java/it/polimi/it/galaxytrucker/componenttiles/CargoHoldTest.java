package it.polimi.it.galaxytrucker.componenttiles;

import it.polimi.it.galaxytrucker.model.componenttiles.CargoHold;
import it.polimi.it.galaxytrucker.model.componenttiles.TileEdge;
import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.model.utility.Cargo;
import it.polimi.it.galaxytrucker.model.utility.Color;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CargoHoldTest {

    @Test
    void getContainerNumber() {
    }

    @Test
    void getContainedCargo() {

    }

    @Test
    void removeCargo() {
        CargoHold hold = new CargoHold(3, List.of(TileEdge.SMOOTH, TileEdge.SMOOTH, TileEdge.SMOOTH, TileEdge.SMOOTH));

        Cargo blue = new Cargo(Color.BLUE);
        Cargo green = new Cargo(Color.GREEN);
        Cargo yellow = new Cargo(Color.YELLOW);

        /* ===== TEST 1 ==== */

        /*
         *  Removing cargo from an empty CargoHold
         */
        Exception exception1 = assertThrows(InvalidActionException.class, () -> {
            hold.removeCargo(0);
        });

        assertEquals("CargoHold is empty", exception1.getMessage());

        /* ===== TEST 2 ==== */

        /*
         *  Removing cargo from a non-empty CargoHold, at an invalid index.
         *  Expect an InvalidActionException to be thrown.
         */

        hold.addCargo(blue);

        Exception exception3 = assertThrows(InvalidActionException.class, () -> {
            hold.removeCargo(10);
        });

        assertEquals("Index out of bounds", exception3.getMessage());

        /* ===== TEST 3 ==== */

        /*
         *  Removing cargo.
         */

        hold.addCargo(green);
        hold.addCargo(yellow);

        Cargo removed1 = hold.removeCargo(0);

        assertEquals(blue, removed1);
        assertEquals(List.of(green, yellow), hold.getContainedCargo());
    }

    @Test
    void addCargo() {
        CargoHold hold = new CargoHold(3, List.of(TileEdge.SMOOTH, TileEdge.SMOOTH, TileEdge.SMOOTH, TileEdge.SMOOTH));

        Cargo blue = new Cargo(Color.BLUE);
        Cargo green = new Cargo(Color.GREEN);
        Cargo yellow = new Cargo(Color.YELLOW);
        Cargo red = new Cargo(Color.RED);


        /* ===== TEST 1 ==== */

        /*
         *  Adding cargo.
         */

        hold.addCargo(blue);
        hold.addCargo(green);
        hold.addCargo(yellow);

        assertEquals(List.of(blue, green, yellow), hold.getContainedCargo());
        assertEquals(3, hold.getContainedCargo().size());

        /* ===== TEST 2 ==== */

        /*
         *  Adding more cargo than the CargoHold has space for.
         *  Expect an InvalidActionException to be thrown.
         */

        Exception exception1 = assertThrows(InvalidActionException.class, () -> {
            hold.addCargo(red);
        });

        assertEquals("CargoHold is already full, can't add cargo", exception1.getMessage());
    }
}