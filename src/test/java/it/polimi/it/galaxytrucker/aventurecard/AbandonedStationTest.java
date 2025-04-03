package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.model.aventurecard.AbandonedStation;
import it.polimi.it.galaxytrucker.model.componenttiles.CabinModule;
import it.polimi.it.galaxytrucker.model.componenttiles.SpecialCargoHold;
import it.polimi.it.galaxytrucker.model.componenttiles.TileEdge;
import it.polimi.it.galaxytrucker.model.crewmates.Human;
import it.polimi.it.galaxytrucker.model.managers.CargoManager;
import it.polimi.it.galaxytrucker.model.managers.FlightBoardState;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.Cargo;
import it.polimi.it.galaxytrucker.model.utility.Color;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AbandonedStationTest {

    @Test
    void applyFlightDayPenalty() {
        AbandonedStation station = new AbandonedStation(Optional.of(3),Optional.of(3),null,0,5,null);
        Player player1=new Player(new UUID(0,1), "Margarozzo1",0, Color.RED);
        FlightBoardState board = new FlightBoardState(18);
        board.setBoard();
        board.addPlayerMarker(player1.getPlayerID(),1);

        station.applyFlightDayPenalty(board,player1);
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
        CargoManager cargoManager = new CargoManager();
        manager.addComponentTile(7,6, new SpecialCargoHold(2,List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));



        AbandonedStation station = new AbandonedStation(Optional.of(2),
                                                        Optional.of(2),
                                                        Optional.of(Set.of(new Cargo(Color.RED), new Cargo(Color.GREEN))),
                                                        0,5,cargoManager);

        Player player1=new Player(new UUID(0,1), "Margarozzo1",0, Color.RED,manager);
        Player player2=new Player(new UUID(0,2), "Margarozzo2",0, Color.YELLOW);
        List<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        station.setPlayer(players);
        FlightBoardState board = new FlightBoardState(18);
        board.addPlayerMarker(player1.getPlayerID(),1);
        board.addPlayerMarker(player2.getPlayerID(),2);

        station.RequiredHumanVerification(board);

        assertEquals(board.getPlayerPosition().get(player1.getPlayerID()), 1);
        assertEquals(player1.getShipManager().getCargoPositon().get(Color.RED),Set.of(List.of(7,6)));
        assertEquals(player1.getShipManager().getCargoPositon().get(Color.GREEN),Set.of(List.of(7,6)));
        assertEquals(station.getIsTaken(), true);
        assertEquals(board.getPlayerPosition().get(player2.getPlayerID()), 2);
    }


}