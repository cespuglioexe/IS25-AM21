package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.model.aventurecard.cards.Smugglers;
import it.polimi.it.galaxytrucker.model.managers.FlightBoard;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.utility.Color;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@Disabled
class SmugglersTest {


    @Test
    void applyPenalty() {

    }

    @Test
    void applyFlightDayPenalty() {
        Smugglers smuggler = new Smugglers(Optional.of(3),Optional.of(2),null,4,5, null);
        Player player1=new Player(new UUID(0,1), "Margarozzo1",0, Color.RED);
        FlightBoard board = new FlightBoard(18);
        board.setBoard();
        board.addPlayerMarker(player1);

        smuggler.applyFlightDayPenalty(board,player1);
        assertEquals(board.getPlayerPosition().get(player1.getPlayerID()),2);
    }
}