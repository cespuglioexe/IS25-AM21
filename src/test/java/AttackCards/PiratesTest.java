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
        int protect = 0;
        List<Shield> shieldActivated = new ArrayList<>();

        for(Map.Entry<Projectile,Direction> entry : projectile.entrySet()){

                /*if(entry.getValue()== shield.getOrientation().getFirst() ||entry.getValue()== shield.getOrientation().get(1) && entry.getKey() == Projectile.SMALL){
                    protect = 1;
                    break;
                }*/

            if(protect!=1){
                if(entry.getValue()== Direction.UP||entry.getValue()== Direction.DOWN){
                    destroyComponent();
                }else{
                    destroyComponent();
                }
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