package it.polimi.it.galaxytrucker.model.aventurecard.cards;

import it.polimi.it.galaxytrucker.model.aventurecard.AdventureCard;
import it.polimi.it.galaxytrucker.model.componenttiles.*;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.utility.Direction;
import it.polimi.it.galaxytrucker.model.utility.Projectile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class MeteorStorm extends AdventureCard {
    private ComponentTile componentHit;
    private int index,line;


    public MeteorStorm(Optional penalty, Optional flightDayPenalty, Optional reward, int firePowerRequired, int creditReward) {
        super(penalty, flightDayPenalty, reward, firePowerRequired, creditReward);
    }

    public void checkComponentHit(Player player, Map<Projectile,Direction> projectile, int lines) {
        List<Optional<ComponentTile>> sequence;

        for(Map.Entry<Projectile,Direction> entry : projectile.entrySet()){
            if (entry.getValue() == Direction.UP || entry.getValue() == Direction.DOWN) {
                sequence = player.getShipManager().getComponentsAtColumn(lines);
            } else {
                sequence = player.getShipManager().getComponentsAtRow(lines);
            }

            line = lines;

            index=4;
            for (Optional<ComponentTile> c : sequence) {
                if (c.isPresent()) {
                    break;
                }
                index++;
            }

        }
    }


    public void meteorStorm(Player player, List<Shield> shieldActivated, Map<Projectile, Direction> projectile, List<Cannon> cannons) {
        int r, protect=0,i=0;
        Set<List<Integer>> sCannonsCoord,dCannonsCoord,allCannonsCoord = null;
        ComponentTile cannon;

        sCannonsCoord = player.getShipManager().getAllComponentsPositionOfType(SingleCannon.class);
        dCannonsCoord = player.getShipManager().getAllComponentsPositionOfType(DoubleCannon.class);
        allCannonsCoord.addAll(sCannonsCoord);
        allCannonsCoord.addAll(dCannonsCoord);

        for(Map.Entry<Projectile,Direction> entry : projectile.entrySet()){

            r = entry.getValue().ordinal();

            if (entry.getKey() == Projectile.SMALL){
                if (componentHit.getTileEdges().get(r) == TileEdge.SMOOTH){
                    continue;
                }

                for (Shield shield : shieldActivated) {
                    if(entry.getValue()== shield.getOrientation().getFirst() ||entry.getValue()== shield.getOrientation().get(1)){
                        protect=1;
                        break;
                    }

                }
                if(protect!=1){
                    if(entry.getValue()== Direction.UP||entry.getValue()== Direction.DOWN){
                        destroyComponent(player,index,line);
                    }else{
                        destroyComponent(player,line, index);
                    }
                }
            }

            if (entry.getKey() == Projectile.BIG){
                for (List<Integer> list : allCannonsCoord) {

                    cannon = player.getShipManager().getComponent(list.getFirst(),list.get(1)).orElse(null);

                    if(entry.getValue().ordinal() == cannon.getRotation()){
                        if(entry.getValue()== Direction.UP){
                            if(line == list.get(1)) {
                                protect=1;
                                break;
                            }
                        }else if (entry.getValue()== Direction.DOWN){
                            if (line == list.get(1)||line == list.get(1)+1||line == list.get(1)-1) {
                                protect=1;
                                break;
                            }
                        }else{
                            if(line == list.get(0)||line == list.get(0)+1||line == list.get(0)-1) {
                                protect=1;
                                break;
                            }
                        }
                    }
                }
                if (protect!=1){
                    if(entry.getValue()== Direction.UP||entry.getValue()== Direction.DOWN){
                        destroyComponent(player,index,line);
                    }else{
                        destroyComponent(player,line, index);
                    }
                }
            }
        }
    }

    public void destroyComponent(Player player, int row, int col) {
        player.getShipManager().removeComponentTile(row,col);
    }

}
