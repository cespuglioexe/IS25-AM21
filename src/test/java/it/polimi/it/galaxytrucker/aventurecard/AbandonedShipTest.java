package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.componenttiles.CabinModule;
import it.polimi.it.galaxytrucker.componenttiles.TileEdge;
import it.polimi.it.galaxytrucker.crewmates.Human;
import it.polimi.it.galaxytrucker.managers.FlightBoardState;
import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.managers.ShipManager;
import it.polimi.it.galaxytrucker.utility.Color;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AbandonedShipTest {

    @Test
    void giveCreditReward() {
        AbandonedShip ship = new AbandonedShip(Optional.of(3),Optional.of(2),Optional.of(5),0,5);
        Player player1=new Player(new UUID(0,1), "Margarozzo1",0, Color.RED);
        ship.giveCreditReward(player1);
        assertEquals(player1.getCredits(),5);
    }

    @Test
    void applyFlightDayPenalty() {
        AbandonedShip ship = new AbandonedShip(Optional.of(2),Optional.of(3),null,0,5);
        Player player1=new Player(new UUID(0,1), "Margarozzo1",0, Color.RED);
        FlightBoardState board = new FlightBoardState(18);
        board.addPlayerMarker(player1.getPlayerID(),1);

        ship.applyFlightDayPenalty(board,player1);
        assertEquals(board.getPlayerPosition().get(player1.getPlayerID()),1);
    }

    @Test
    void requiredHumanVerification() {
        ShipManager manager = new ShipManager(1);
        manager.addComponentTile(6,7, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        manager.addComponentTile(6,8, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        manager.addCrewmate(6,7, new Human());
        manager.addCrewmate(6,7, new Human());
        manager.addCrewmate(6,8, new Human());
        manager.addCrewmate(6,8, new Human());

        AbandonedShip ship = new AbandonedShip(Optional.of(2),Optional.of(2),Optional.of(0),0,5);
        Player player1=new Player(new UUID(0,1), "Margarozzo1",0, Color.RED,manager);
        Player player2=new Player(new UUID(0,2), "Margarozzo2",0, Color.YELLOW);
        List<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        ship.setPlayer(players);
        FlightBoardState board = new FlightBoardState(18);
        board.addPlayerMarker(player1.getPlayerID(),1);
        board.addPlayerMarker(player2.getPlayerID(),2);
        ship.RequiredHumanVerification(board);

        assertEquals(board.getPlayerPosition().get(player1.getPlayerID()), 1);
        assertEquals(player1.getCredits(), 5);
        assertEquals(ship.getIsTaken(), true);
        assertEquals(board.getPlayerPosition().get(player2.getPlayerID()), 2);
        assertEquals(player2.getCredits(), 0);
    }


}