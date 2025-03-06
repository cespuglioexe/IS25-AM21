package managers;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import it.polimi.it.galaxytrucker.componenttiles.ComponentTile;
import it.polimi.it.galaxytrucker.managers.ShipBoard;

public class ShipBoardTest {

    @Test
    void correctShipBoardSizeTest() {
        ShipBoard ship = new ShipBoard();

        assertEquals(ship.getBoard().size(), 5);
        assertEquals(ship.getBoard().get(0).size(), 7);
    }
    
    @Test
    void correctShipBoardTest() {
        ShipBoard ship = new ShipBoard();

        for (int i = 0; i < ship.getBoard().size(); i++) {
            for (int j = 0; j < ship.getBoard().get(i).size(); j++) {
                assertEquals(ship.getBoard().get(i).get(j), Optional.<ComponentTile>empty());
                System.out.print("[ ]");
            }
            System.out.println();
        }
    }
}
