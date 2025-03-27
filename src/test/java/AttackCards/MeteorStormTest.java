package AttackCards;

import it.polimi.it.galaxytrucker.componenttiles.*;
import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.utility.Color;
import it.polimi.it.galaxytrucker.utility.Direction;
import it.polimi.it.galaxytrucker.utility.Projectile;
import org.junit.jupiter.api.Test;

import java.util.*;

class MeteorStormTest {
    private Player p1 = new Player(UUID.randomUUID(),"sergio",10, Color.BLUE);
    private Player p2 = new Player(UUID.randomUUID(),"jack",10, Color.RED);
    private int line = 6, index = 4;
    private List<Player> players = new ArrayList<>();
    private Map<Projectile, Direction> projectile = new HashMap<>();

    @Test
    void checkComponentHit() {
        projectile.put(Projectile.BIG,Direction.UP);
        List<Optional<ComponentTile>> sequence = new ArrayList<>();
        ComponentTile componentTile = new CabinModule(List.of(TileEdge.SMOOTH,TileEdge.SMOOTH,TileEdge.SMOOTH,TileEdge.SMOOTH));
        sequence.add(Optional.empty());
        sequence.add(Optional.empty());
        sequence.add(Optional.of(componentTile));

        for(Map.Entry<Projectile,Direction> entry : projectile.entrySet()){

            for (Optional<ComponentTile> c : sequence) {
                if (c.isPresent()) {
                    break;
                }
                index++;
            }

            if (entry.getValue() == Direction.UP || entry.getValue() == Direction.DOWN) {
                System.out.println("Il meteorite arriva da sopra/sotto");
                System.out.println("Riga:" + index);
                System.out.println("Colonna" + line);
            } else {
                System.out.println("Il meteorite arriva da sx/dx");
                System.out.println("Riga:" + line);
                System.out.println("Colonna" + index);
            }
        }
    }

    @Test
    void meteorStorm() {
        int r, protect=0;
        Player player = new Player(UUID.randomUUID(),"sergio",10, Color.BLUE);

        Shield shield = new Shield(Direction.UP, List.of(TileEdge.SINGLE, TileEdge.DOUBLE,TileEdge.SMOOTH,TileEdge.SINGLE));
        List<Shield> shieldActivated = new ArrayList<>();
        shieldActivated.add(shield);

        SingleCannon cannon = new SingleCannon(List.of(TileEdge.SINGLE, TileEdge.DOUBLE,TileEdge.SMOOTH,TileEdge.SINGLE));
        Set<List<Integer>> allCannonsCoord = new HashSet<>();
        List<Integer> coord = Arrays.asList(5,6);
        List<Integer> coord2 = Arrays.asList(7,10);
        allCannonsCoord.add(coord);
        allCannonsCoord.add(coord2);

        projectile.put(Projectile.BIG,Direction.UP);

        ComponentTile componentHit = new StructuralModule(List.of(TileEdge.SINGLE, TileEdge.DOUBLE,TileEdge.SMOOTH,TileEdge.SINGLE));


        for(Map.Entry<Projectile, Direction> entry : projectile.entrySet()){

            r = entry.getValue().ordinal();
            System.out.println("Rotazione: " + r);

            if (entry.getKey() == Projectile.SMALL){
                System.out.println("Lato: " + componentHit.getTileEdges().get(r));
                if (componentHit.getTileEdges().get(r) == TileEdge.SMOOTH){
                    System.out.println("Meteorite piccolo e lato liscio: nessun danno");
                    continue;
                }

                for (Shield s : shieldActivated) {
                    if(entry.getValue()== s.getOrientation().getFirst() ||entry.getValue()== s.getOrientation().get(1)){
                        System.out.println("Orientazione scudo: " + s.getOrientation().getFirst() + " - "+s.getOrientation().get(1));
                        protect=1;
                        break;
                    }

                }
                if(protect!=1){
                    if(entry.getValue()== Direction.UP||entry.getValue()== Direction.DOWN){
                        destroyComponent();
                        System.out.println("Distruzione pezzo da sopra/sotto");
                    }else{
                        System.out.println("Distruzione pezzo da dx/sx");
                        destroyComponent();
                    }
                }
            }

            if (entry.getKey() == Projectile.BIG ){
                for (List<Integer> listCannons : allCannonsCoord) {

                    System.out.println("Coordinate del cannone:" + listCannons);
                    System.out.println("Rotazione del cannone" + cannon.getRotation());

                    if(entry.getValue().ordinal() == cannon.getRotation()){
                        System.out.println("Il cannone è orientato correttamente");

                        if(entry.getValue()== Direction.UP){
                            if(line == listCannons.get(1)) {
                                protect=1;
                                break;
                            }
                        }else if (entry.getValue()== Direction.DOWN){
                            if (line == listCannons.get(1)||line == listCannons.get(1)+1||line == listCannons.get(1)-1) {
                                protect=1;
                                break;
                            }
                        }else{
                            if(line == listCannons.get(0)||line == listCannons.get(0)+1||line == listCannons.get(0)-1) {
                                protect=1;
                                break;
                            }
                        }
                    }

                }
                if (protect!=1){
                    if(entry.getValue()== Direction.UP||entry.getValue()== Direction.DOWN){
                        System.out.println("Distruzione pezzo da sopra/sotto");
                    }else{
                        System.out.println("Distruzione pezzo da dx/sx");

                    }
                }else{
                    System.out.println("Meteorite colpito dal cannone");
                }
            }
        }
    }

    @Test
    void destroyComponent() {
    }
}