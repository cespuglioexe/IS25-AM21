package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.model.aventurecard.cards.OpenSpace;
import it.polimi.it.galaxytrucker.model.managers.FlightBoard;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.utility.Color;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OpenSpaceTest {

    @Test
    void travel() {
        OpenSpace space = new OpenSpace(null, Optional.of(5),null,0,0);
        Player player1=new Player(new UUID(0,1), "Margarozzo1",0, Color.RED);
        FlightBoard board = new FlightBoard(18);
        board.setBoard();
        board.addPlayerMarker(player1);

        space.travel(board,player1,5);
        assertEquals(board.getPlayerPosition().get(player1.getPlayerID()),9);
    }
}