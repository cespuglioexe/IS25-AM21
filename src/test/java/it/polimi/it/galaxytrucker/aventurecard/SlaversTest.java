import it.polimi.it.galaxytrucker.aventurecard.AbandonedShip;
import it.polimi.it.galaxytrucker.aventurecard.Slavers;
import it.polimi.it.galaxytrucker.managers.FlightBoardState;
import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.utility.Color;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SlaversTest {

    @Test
    void checkReward() {

        int firePower=6;
        Slavers slaver = new Slavers(3,2,5,4,5);
        assertEquals(slaver.checkReward(firePower), true);
        slaver.setDefeated(false);
        firePower=4;
        assertEquals(slaver.checkReward(firePower), true);
        slaver.setDefeated(false);
        firePower=3;
        assertEquals(slaver.checkReward(firePower), false);
        firePower=7;
        assertEquals(slaver.checkReward(firePower), false);

    }

    @Test
    void giveCreditReward() {
        Slavers slaver = new Slavers(3,2,5,4,5);
        Player player1=new Player(new UUID(0,1), "Margarozzo1",0, Color.RED);
        ship.giveCreditReward(player1);
        assertEquals(player1.getCredits(),5);
    }

    @Test
    void applyCrewmatePenalty() {
    }

    @Test
    void applyFlightDayPenalty() {
        Slavers slaver = new Slavers(3,2,5,4,5);
        Player player1=new Player(new UUID(0,1), "Margarozzo1",0, Color.RED);
        FlightBoardState board = new FlightBoardState(18);
        board.setBoard();
        board.addPlayerMarker(player1,1);

        slaver.applyFlightDayPenalty();
        assertEquals(board.getPlayerPosition().get(player1.getPlayerID()),1);
    }
}