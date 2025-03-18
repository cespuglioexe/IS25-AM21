import it.polimi.it.galaxytrucker.aventurecard.Slavers;
import it.polimi.it.galaxytrucker.aventurecard.Smugglers;
import it.polimi.it.galaxytrucker.managers.FlightBoardState;
import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.utility.Color;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@Disabled
class SmugglersTest {

    @Test
    void checkReward() {
        int firePower=6;
        Smugglers smuggler = new Smugglers(3,2,5,4,5),null;
        assertEquals(smuggler.checkReward(firePower), true);
        smuggler.setDefeated(false);
        firePower=4;
        assertEquals(smuggler.checkReward(firePower), true);
        smuggler.setDefeated(false);
        firePower=3;
        assertEquals(smuggler.checkReward(firePower), false);
        firePower=7;
        assertEquals(smuggler.checkReward(firePower), false);
    }

    @Test
    void applyPenalty() {

    }

    @Test
    void applyFlightDayPenalty() {
        Smugglers smuggler = new Smugglers(3,2,5,4,5);
        Player player1=new Player(new UUID(0,1), "Margarozzo1",0, Color.RED);
        FlightBoardState board = new FlightBoardState(18);
        board.setBoard();
        board.addPlayerMarker(player1,1);

        smuggler.applyFlightDayPenalty();
        assertEquals(board.getPlayerPosition().get(player1.getPlayerID()),1);
    }
}