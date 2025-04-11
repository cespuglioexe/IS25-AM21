package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.model.aventurecard.cards.StarDust;
import it.polimi.it.galaxytrucker.model.componenttiles.CabinModule;
import it.polimi.it.galaxytrucker.model.componenttiles.TileEdge;
import it.polimi.it.galaxytrucker.model.managers.FlightBoard;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.Color;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class StarDustTest {

    @Test
    void applyFlightDayPenalty() {

        ShipManager manager = new ShipManager(1);
        manager.addComponentTile(6,7, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        manager.addComponentTile(6,6, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        StarDust star = new StarDust(null,null,null,0,0);
        Player player1=new Player(new UUID(0,1), "Margarozzo1",0, Color.RED, manager);
        FlightBoard board = new FlightBoard(18);
        board.setBoard();
        board.addPlayerMarker(player1);

        star.applyFlightDayPenalty(board,player1);
        assertEquals(board.getPlayerPosition().get(player1.getPlayerID()),14);
    }
}