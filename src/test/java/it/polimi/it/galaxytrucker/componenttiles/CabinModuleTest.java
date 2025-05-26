package it.polimi.it.galaxytrucker.componenttiles;

import it.polimi.it.galaxytrucker.model.componenttiles.CabinModule;
import it.polimi.it.galaxytrucker.model.componenttiles.TileEdge;
import it.polimi.it.galaxytrucker.model.crewmates.Alien;
import it.polimi.it.galaxytrucker.model.crewmates.Crewmate;
import it.polimi.it.galaxytrucker.model.crewmates.Human;
import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.model.utility.AlienType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CabinModuleTest {

    @Test
    void testAddHuman() {
        CabinModule cabin = new CabinModule(List.of(TileEdge.SMOOTH, TileEdge.SMOOTH, TileEdge.SMOOTH, TileEdge.SMOOTH), null);


        Human human1 = new Human();
        Human human2 = new Human();
        Human human3 = new Human();

        Alien alien1 = new Alien(AlienType.PURPLEALIEN);
        Alien alien2 = new Alien(AlienType.BROWNALIEN);


        List<Crewmate> crewmates = new ArrayList<>();

        /* ===== TEST 1 ==== */

        /*
         *  Adding the same {@code Human} to both the test list and the cabin. Expect both lists to
         *  be the same
         */
        crewmates.add(human1);
        cabin.addCrewmate(human1);

        assertEquals(1, cabin.getCrewmates().size());
        assertEquals(crewmates, cabin.getCrewmates());

        /* ===== TEST 2 ==== */

        /*
         *  Adding the same {@code Human} to both the test list and the cabin. Expect both lists to
         *  be the same
         */
        crewmates.add(human2);
        cabin.addCrewmate(human2);

        assertEquals(2, cabin.getCrewmates().size());
        assertEquals(crewmates, cabin.getCrewmates());

        /* ===== TEST 3 ==== */

        /*
         *  Adding a third {@code Human} to the cabin, expect to get an exception and {@code cabin.crewmates}
         *  to remain the same
         */
        Exception exception1 = assertThrows(InvalidActionException.class, () -> {
            cabin.addCrewmate(human3);
        });

        assertEquals("Cabin already full", exception1.getMessage());

        assertEquals(2, cabin.getCrewmates().size());
        assertEquals(crewmates, cabin.getCrewmates());

        /* ===== TEST 4 ==== */

        /*
         *  Adding an {@code Alien} to a cabin with two {@code Human}, expect to get an exception
         *  and {@code cabin.crewmates} to remain the same
         */
        Exception exception2 = assertThrows(InvalidActionException.class, () -> {
            cabin.addCrewmate(alien1);
        });

        assertEquals("Cabin must be empty for alien", exception2.getMessage());

        assertEquals(2, cabin.getCrewmates().size());
        assertEquals(crewmates, cabin.getCrewmates());

        /* ===== TEST 5 ==== */

        /*
         *  Removing the first {@code Crewmate} from both the test list and the cabin. Expect both lists to
         *  be the same and for the {@code Crewmate} removed from the cabin to be {@code human1}
         */
        crewmates.removeFirst();
        Crewmate removed1 = cabin.removeCrewmate();

        assertEquals(1, cabin.getCrewmates().size());
        assertEquals(crewmates, cabin.getCrewmates());
        assertEquals(human1, removed1);

        /* ===== TEST 6 ==== */

        /*
         *  Adding an {@code Alien} to a cabin with one {@code Human}, expect to get an exception
         *  and {@code cabin.crewmates} to remain the same
         */
        Exception exception3 = assertThrows(InvalidActionException.class, () -> {
            cabin.addCrewmate(alien1);
        });

        assertEquals("Cabin must be empty for alien", exception3.getMessage());

        assertEquals(1, cabin.getCrewmates().size());
        assertEquals(crewmates, cabin.getCrewmates());

        /* ===== TEST 7 ==== */

        /*
         *  Removing the first {@code Crewmate} from both the test list and the cabin. Expect both lists to
         *  be the same, for the {@code Crewmate} removed from the cabin to be {@code human1} and for both lists
         *  empty
         */
        crewmates.removeFirst();
        Crewmate removed2 = cabin.removeCrewmate();

        assertEquals(0, cabin.getCrewmates().size());
        assertEquals(crewmates, cabin.getCrewmates());
        assertTrue(crewmates.isEmpty());
        assertTrue(cabin.getCrewmates().isEmpty());
        assertEquals(human2, removed2);

        /* ===== TEST 8 ==== */

        /*
         *  Adding an {@code Alien} to an empty cabin and  the test list. Expect both lists to
         *  be the same
         */
        crewmates.add(alien1);
        cabin.addCrewmate(alien1);

        assertEquals(1, cabin.getCrewmates().size());
        assertEquals(crewmates, cabin.getCrewmates());

        /* ===== TEST 9 ==== */

        /*
         *  Adding an {@code Alien} to a cabin with another alien and to the test list. Expect an exception
         *  and for both lists to remain the same
         */
        Exception exception4 = assertThrows(InvalidActionException.class, () -> {
            cabin.addCrewmate(alien2);
        });

        assertEquals("Cabin must be empty for alien", exception4.getMessage());

        assertEquals(1, cabin.getCrewmates().size());
        assertEquals(crewmates, cabin.getCrewmates());

        /* ===== TEST 9 ==== */

        /*
         *  Adding a {@code Human} to a cabin with an alien and to the test list. Expect an exception
         *  and for both lists to remain the same
         */
        Exception exception5 = assertThrows(InvalidActionException.class, () -> {
            cabin.addCrewmate(human1);
        });

        assertEquals("Cabin contains Alien, can't add Human", exception5.getMessage());

        assertEquals(1, cabin.getCrewmates().size());
        assertEquals(crewmates, cabin.getCrewmates());
    }
}