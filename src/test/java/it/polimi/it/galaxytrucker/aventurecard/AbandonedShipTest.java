import it.polimi.it.galaxytrucker.aventurecard.AbandonedShip;
import it.polimi.it.galaxytrucker.aventurecard.CargoManager;
import it.polimi.it.galaxytrucker.aventurecard.Planet;
import it.polimi.it.galaxytrucker.componenttiles.CabinModule;
import it.polimi.it.galaxytrucker.crewmates.Human;
import it.polimi.it.galaxytrucker.managers.FlightBoardState;
import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.managers.ShipManager;
import it.polimi.it.galaxytrucker.utility.Cargo;
import it.polimi.it.galaxytrucker.utility.Color;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AbandonedShipTest {

    @Test
    void giveCreditReward() {
        AbandonedShip ship = new AbandonedShip(3,2,5,0,5);
        Player player1=new Player(new UUID(0,1), "Margarozzo1",0, Color.RED);
        ship.giveCreditReward(player1);
        assertEquals(player1.getCredits(),5);
    }


    @Test
    void applyCrewmatePenalty() {

    }

    @Test
    void applyFlightDayPenalty() {
        AbandonedShip ship = new AbandonedShip(2,3,0,0,5);
        Player player1=new Player(new UUID(0,1), "Margarozzo1",0, Color.RED);
        FlightBoardState board = new FlightBoardState(18);
        board.setBoard();
        board.addPlayerMarker(player1,1);

        ship.applyFlightDayPenalty();
        assertEquals(board.getPlayerPosition().get(player1.getPlayerID()),1);
    }

    @Test
    void requiredHumanVerification() {
        ShipManager manager = new ShipManager(1);
        manager.addComponentTile(6,7, CabinModule);
        manager.addComponentTile(6,8, CabinModule);
        manager.addCrewmate(6,7, Human);
        manager.addCrewmate(6,7, Human);
        manager.addCrewmate(6,8, Human);
        manager.addCrewmate(6,8, Human);

        AbandonedShip ship = new AbandonedShip(2,3,0,0,5);
        Player player1=new Player(new UUID(0,1), "Margarozzo1",0, Color.RED,manager);

        assertEquals(ship.RequiredHumanVerification(player1),true);

        player1.getShipManager().removeCrewmate(6,7);
        player1.getShipManager().removeCrewmate(6,7);

        assertEquals(ship.RequiredHumanVerification(player1),false);
    }


}