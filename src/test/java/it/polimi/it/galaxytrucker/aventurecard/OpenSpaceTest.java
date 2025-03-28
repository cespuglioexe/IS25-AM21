package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.aventurecard.AbandonedShip;
import it.polimi.it.galaxytrucker.aventurecard.OpenSpace;
import it.polimi.it.galaxytrucker.managers.FlightBoardState;
import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.utility.Color;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OpenSpaceTest {

    @Test
    void travel() {
        OpenSpace space = new OpenSpace(null, Optional.of(5),null,0,0);
        Player player1=new Player(new UUID(0,1), "Margarozzo1",0, Color.RED);
        FlightBoardState board = new FlightBoardState(18);
        board.setBoard();
        board.addPlayerMarker(player1.getPlayerID(),1);

        space.travel(board,player1,5);
        assertEquals(board.getPlayerPosition().get(player1.getPlayerID()),9);
    }
}