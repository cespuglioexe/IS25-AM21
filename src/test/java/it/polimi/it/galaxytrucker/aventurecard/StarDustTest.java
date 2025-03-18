import it.polimi.it.galaxytrucker.aventurecard.AbandonedShip;
import it.polimi.it.galaxytrucker.aventurecard.StarDust;
import it.polimi.it.galaxytrucker.componenttiles.CabinModule;
import it.polimi.it.galaxytrucker.componenttiles.TileEdge;
import it.polimi.it.galaxytrucker.managers.FlightBoardState;
import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.managers.ShipManager;
import it.polimi.it.galaxytrucker.utility.Color;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class StarDustTest {

    @Test
    void applyFlightDayPenalty() {

        ShipManager manager = new ShipManager(1);
        manager.addComponentTile(6,7, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        manager.addComponentTile(6,9, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        StarDust star = new StarDust(null,null,null,0,0);
        Player player1=new Player(new UUID(0,1), "Margarozzo1",0, Color.RED);
        FlightBoardState board = new FlightBoardState(18);
        board.setBoard();
        board.addPlayerMarker(player1.getPlayerID(),1);

        star.applyFlightDayPenalty(board,player1);
        assertEquals(board.getPlayerPosition().get(player1.getPlayerID()),17);
    }
}