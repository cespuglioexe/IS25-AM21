package AttackCards;

import it.polimi.it.galaxytrucker.componenttiles.*;
import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.utility.Color;

import it.polimi.it.galaxytrucker.utility.Direction;
import it.polimi.it.galaxytrucker.utility.Projectile;
import org.junit.jupiter.api.Test;

import java.util.*;

class PiratesTest {
    private int index=4;
    private int lines = 7;
    Player player = new Player(UUID.randomUUID(),"sergio",10,Color.BLUE);
    Map<Projectile, Direction> projectile = new HashMap<>();
    ComponentTile componentHit = null;

    @Test
    void checkIfPlayerLoseToPirates() {
       int piratesfp = 10, pfp = 9;
       if(pfp < piratesfp){
            System.out.println("Player Lost");
       }else {
           System.out.println("Player Won");
       }
    }

    @Test
    void checkComponentHit() {
        projectile.put(Projectile.SMALL,Direction.UP);
        List<Optional<ComponentTile>> sequence = new ArrayList<>();
        ComponentTile componentTile = new CabinModule(List.of(TileEdge.SMOOTH,TileEdge.SMOOTH,TileEdge.SMOOTH,TileEdge.SMOOTH));
        sequence.add(Optional.empty());
        sequence.add(Optional.empty());
        sequence.add(Optional.of(componentTile));

        for(Map.Entry<Projectile,Direction> entry : projectile.entrySet()){

            for (Optional<ComponentTile> c : sequence) {
                if (c.isPresent()) {
                    componentHit = sequence.get(index-4).orElse(null);
                    System.out.println(sequence.get(index-4));
                    break;
                }
                index++;
            }
            System.out.println(componentHit.getTileEdges());

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
    void giveCreditReward() {
    }

    @Test
    void applyFlightDayPenalty() {
    }
}