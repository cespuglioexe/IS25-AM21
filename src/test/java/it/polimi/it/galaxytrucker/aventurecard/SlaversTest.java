package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.aventurecard.AbandonedShip;
import it.polimi.it.galaxytrucker.aventurecard.Slavers;
import it.polimi.it.galaxytrucker.managers.FlightBoardState;
import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.utility.Color;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SlaversTest {


    @Test
    void giveCreditReward() {
        Slavers slaver = new Slavers(Optional.of(3),Optional.of(2),null,4,5);
        Player player1=new Player(new UUID(0,1), "Margarozzo1",0, Color.RED);
        slaver.giveCreditReward(player1);
        assertEquals(player1.getCredits(),5);
    }

    @Test
    void applyFlightDayPenalty() {
        Slavers slaver = new Slavers(Optional.of(3),Optional.of(2),null,4,5);
        Player player1=new Player(new UUID(0,1), "Margarozzo1",0, Color.RED);
        FlightBoardState board = new FlightBoardState(18);
        board.setBoard();
        board.addPlayerMarker(player1.getPlayerID(),1);

        slaver.applyFlightDayPenalty(board,player1);
        assertEquals(board.getPlayerPosition().get(player1.getPlayerID()),2);
    }
}