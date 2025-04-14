package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.model.adventurecards.cards.AbandonedShip;
import it.polimi.it.galaxytrucker.model.componenttiles.CabinModule;
import it.polimi.it.galaxytrucker.model.componenttiles.TileEdge;
import it.polimi.it.galaxytrucker.model.crewmates.Human;
import it.polimi.it.galaxytrucker.model.managers.FlightBoard;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.Color;
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
        FlightBoard board = new FlightBoard(18);
        board.addPlayerMarker(player1);

        ship.applyFlightDayPenalty(board,player1);
        assertEquals(1, board.getPlayerPosition().get(player1));
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
        FlightBoard board = new FlightBoard(18);
        board.addPlayerMarker(player1);
        board.addPlayerMarker(player2);
        ship.RequiredHumanVerification(board);

        assertEquals(board.getPlayerPosition().get(player1), 1);
        assertEquals(player1.getCredits(), 5);
        assertEquals(ship.getIsTaken(), true);
        assertEquals(board.getPlayerPosition().get(player2), 2);
        assertEquals(player2.getCredits(), 0);
    }


}