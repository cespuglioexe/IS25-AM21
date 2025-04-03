package AttackCards;

import it.polimi.it.galaxytrucker.model.componenttiles.Shield;
import it.polimi.it.galaxytrucker.model.componenttiles.TileEdge;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.utility.Color;
import it.polimi.it.galaxytrucker.model.utility.Direction;
import it.polimi.it.galaxytrucker.model.utility.Projectile;
import org.junit.jupiter.api.Test;

import java.util.*;

class CombatZoneTest {
    private final int FLYPENALTY = 3;
    private final int CREWPENALTY = 2;;
    private List<Integer> fp = new ArrayList<>();
    private List<Integer> ep = new ArrayList<>();
    private List<Integer> cm = new ArrayList<>();
    private Player p1 = new Player(UUID.randomUUID(),"sergio",10, Color.BLUE);
    private Player p2 = new Player(UUID.randomUUID(),"jack",10, Color.RED);
    private int lines = 6, index = 4;
    private List<Player> players = new ArrayList<>();
    Map<Projectile, Direction> projectile = new HashMap<>();


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

        projectile.put(Projectile.SMALL, Direction.UP);
        List<Optional<Integer>> sequence = new ArrayList<>();
        sequence.add(Optional.empty());
        sequence.add(Optional.empty());
        sequence.add(Optional.of(1));

        for(Map.Entry<Projectile,Direction> entry : projectile.entrySet()){


            for (Optional<Integer> c : sequence) {
                if (c.isPresent()) {
                    break;
                }
                index++;
            }

            if (entry.getValue() == Direction.UP || entry.getValue() == Direction.DOWN) {
                System.out.println("Il meteorite arriva da sopra/sotto");
                System.out.println("Riga:" + index);
                System.out.println("Colonna" + lines);
            } else {
                System.out.println("Il meteorite arriva da sx/dx");
                System.out.println("Riga:" + lines);
                System.out.println("Colonna" + index);
            }
        }
    }

    @Test
    void attack() {
        init();

        int protect = 0;
        Shield shield = new Shield(Direction.UP, List.of(TileEdge.SINGLE, TileEdge.DOUBLE,TileEdge.SMOOTH,TileEdge.SINGLE));
        List<Shield> shields = new ArrayList<>();
        shields.add(shield);
        projectile.put(Projectile.SMALL,Direction.UP);


        System.out.println(protect);
        for(Map.Entry<Projectile,Direction> entry : projectile.entrySet()){
            for (Shield shield1 : shields) {
                if(entry.getValue()== shield1.getOrientation().getFirst() ||entry.getValue()== shield1.getOrientation().get(1) && entry.getKey() == Projectile.SMALL){
                    protect = 1;
                    break;
                }
            }

            System.out.println(protect);
            if(protect==0){
                if(entry.getValue()== Direction.UP||entry.getValue()== Direction.DOWN){
                    //destroyComponent();
                    System.out.println("Il componente in posizione "+index+","+lines+" verrà distrutto");
                }else{
                    //destroyComponent();
                    System.out.println("Il componente in posizione "+lines+","+index+" verrà distrutto");
                }
            }else {
                System.out.println("Il componente in posizione "+index+","+lines+" è protetto dallo scudo");
            }
        }

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