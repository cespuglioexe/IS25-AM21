package it.polimi.it.galaxytrucker.componenttiles;

import it.polimi.it.galaxytrucker.model.componenttiles.CentralCabin;
import it.polimi.it.galaxytrucker.model.componenttiles.TileEdge;
import it.polimi.it.galaxytrucker.model.crewmates.Alien;
import it.polimi.it.galaxytrucker.model.crewmates.Crewmate;
import it.polimi.it.galaxytrucker.model.crewmates.Human;
import it.polimi.it.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.model.utility.AlienType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CentralCabinTest {

    private CentralCabin cabin;
    private Human human1;
    private Human human2;
    private Human human3;
    private Alien alien;

    @BeforeEach
    public void setup() {
        // Create a dummy central cabin
        cabin = new CentralCabin(List.of(TileEdge.SMOOTH, TileEdge.SMOOTH, TileEdge.SMOOTH, TileEdge.SMOOTH));

        // Create test crewmates
        human1 = new Human();
        human2 = new Human();
        human3 = new Human();
        alien = new Alien(AlienType.PURPLEALIEN);
    }

    @Test
    public void testAddOneHuman() throws InvalidActionException {
        // Add one human to the cabin
        cabin.addCrewmate(human1);

        // Verify human was added
        assertEquals(1, cabin.getCrewmates().size());
        assertEquals(human1, cabin.getCrewmates().get(0));
    }

    @Test
    public void testAddTwoHumans() throws InvalidActionException {
        // Add two humans to the cabin
        cabin.addCrewmate(human1);
        cabin.addCrewmate(human2);

        // Verify both humans were added in order
        assertEquals(2, cabin.getCrewmates().size());
        assertEquals(human1, cabin.getCrewmates().get(0));
        assertEquals(human2, cabin.getCrewmates().get(1));
    }

    @Test
    public void testAddThirdHuman() throws InvalidActionException {
        // Add two humans to the cabin
        cabin.addCrewmate(human1);
        cabin.addCrewmate(human2);

        // Attempt to add a third human, expect InvalidActionException
        InvalidActionException exception = assertThrows(
                InvalidActionException.class,
                () -> cabin.addCrewmate(human3)
        );

        assertEquals("Cabin already full", exception.getMessage());
        assertEquals(2, cabin.getCrewmates().size());
    }

    @Test
    public void testRemoveCrewmate() throws InvalidActionException {
        // Add two humans to the cabin
        cabin.addCrewmate(human1);
        cabin.addCrewmate(human2);

        // Remove a crewmate and verify it's the first one
        Crewmate removed = cabin.removeCrewmate();
        assertEquals(human1, removed);
        assertEquals(1, cabin.getCrewmates().size());
        assertEquals(human2, cabin.getCrewmates().get(0));
    }

    @Test
    public void testRemoveFromEmptyCabin() {
        cabin = new CentralCabin(List.of(TileEdge.SMOOTH, TileEdge.SMOOTH, TileEdge.SMOOTH, TileEdge.SMOOTH));
        // Attempt to remove from an empty cabin, expect InvalidActionException
        InvalidActionException exception = assertThrows(
                InvalidActionException.class,
                () -> cabin.removeCrewmate()
        );

        assertEquals("Cabin is empty", exception.getMessage());
    }

    @Test
    public void testCantAddHumanWithAlien() {
        // Manually adding an alien, as CentralCabin doesn't support adding aliens
        cabin.crewmates.add(alien);

        // Attempt to add a human, expect InvalidActionException
        InvalidActionException exception = assertThrows(
                InvalidActionException.class,
                () -> cabin.addCrewmate(human1)
        );

        assertEquals("Cabin contains Alien, can't add Human", exception.getMessage());
        assertEquals(1, cabin.getCrewmates().size());
        assertEquals(alien, cabin.getCrewmates().get(0));
    }

    @Test
    public void testRemoveAndThenAddAgain() {
        // Add a human then remove it
        cabin.addCrewmate(human1);
        Crewmate removed = cabin.removeCrewmate();

        assertEquals(human1, removed);
        assertEquals(0, cabin.getCrewmates().size());

        // Add another human
        cabin.addCrewmate(human2);
        assertEquals(1, cabin.getCrewmates().size());
        assertEquals(human2, cabin.getCrewmates().get(0));
    }
}