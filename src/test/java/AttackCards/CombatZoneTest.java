package AttackCards;

import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.utility.Color;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CombatZoneTest {
    private final int FLYPENALTY = 3;
    private final int CREWPENALTY = 2;;
    private List<Integer> fp = new ArrayList<>();
    private List<Integer> ep = new ArrayList<>();
    private List<Integer> cm = new ArrayList<>();
    private Player p1 = new Player(UUID.randomUUID(),"sergio",10, Color.BLUE);
    private Player p2 = new Player(UUID.randomUUID(),"jack",10, Color.RED);

    private List<Player> players = new ArrayList<>();

    void init(){
        players.add(p1);
        players.add(p2);
        fp.add(3);
        ep.add(3);
        cm.add(3);
        fp.add(4);
        ep.add(2);
        cm.add(1);
    }

    private int index,line;

    @Test
    void checkLoserEp() {
        init();
        Player loserEp = players.getFirst();

        for (int i = 1; i < players.size(); i++) {
            System.out.println(ep.get(0));
            System.out.println(ep.get(1));
            if (ep.get(i-1) > ep.get(i)) {
                loserEp = players.get(i);
            }
        }

        System.out.print("Il giocatore con meno potenza motrice è: " + loserEp.getPlayerName());
    }

    @Test
    void checkLoserFp() {
        init();
        Player loserFp = players.getFirst();

        for (int i = 1; i < players.size(); i++) {
            System.out.println(fp.get(0));
            System.out.println(fp.get(1));
            if (fp.get(i-1) > fp.get(i)) {
                loserFp = players.get(i);
            }
        }

        System.out.print("Il giocatore con meno potenza di fuoco è: " + loserFp.getPlayerName());
    }

    @Test
    void checkLoserCm() {
        init();
        Player loserCm = players.getFirst();

        for (int i = 1; i < players.size(); i++) {
            if (cm.get(i-1) > cm.get(i)) {
                loserCm = players.get(i);
            }
        }

        System.out.print("Il giocatore con meno crewmates è: " + loserCm.getPlayerName());
    }

    @Test
    void checkComponentHit() {
    }

    @Test
    void attack() {
        init();
    }

    @Test
    void destroyComponent() {
    }

    @Test
    void applyCrewmatePenalty() {
    }

    @Test
    void applyFlightDayPenalty() {
    }
}