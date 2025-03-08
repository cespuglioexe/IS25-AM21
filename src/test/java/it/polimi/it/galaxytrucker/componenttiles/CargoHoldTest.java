package it.polimi.it.galaxytrucker.componenttiles;

import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.utility.Cargo;
import it.polimi.it.galaxytrucker.utility.Color;
import org.junit.jupiter.api.Test;

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
    }

    @Test
    void addCargo() {
        CargoHold hold = new CargoHold(3, TileEdge.SMOOTH, TileEdge.SMOOTH, TileEdge.SMOOTH, TileEdge.SMOOTH);

        Cargo blue = new Cargo(Color.BLUE);
        Cargo green = new Cargo(Color.GREEN);
        Cargo yellow = new Cargo(Color.YELLOW);
        Cargo red = new Cargo(Color.RED);

        hold.addCargo(blue);
        hold.addCargo(green);
        hold.addCargo(yellow);

        /* ===== TEST 1 ==== */

        /*
         *  Adding an {@code Alien} to a cabin with one {@code Human}, expect to get an exception
         *  and {@code cabin.crewmates} to remain the same
         */
        Exception exception3 = assertThrows(InvalidActionException.class, () -> {

        });

        assertEquals("Cabin must be empty for alien", exception3.getMessage());

        hold.addCargo(red);

    }
}