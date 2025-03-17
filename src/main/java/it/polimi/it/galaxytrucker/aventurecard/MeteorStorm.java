package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.componenttiles.ComponentTile;
import it.polimi.it.galaxytrucker.componenttiles.Shield;
import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.utility.Direction;
import it.polimi.it.galaxytrucker.utility.Projectile;

import java.util.Map;
import java.util.Optional;

public class MeteorStorm extends AdventureCard{
    private ComponentTile componentHit;

    public MeteorStorm(Optional partecipants, Optional penalty, Optional flightDayPenalty, Optional reward, int firePowerRequired, int creditReward) {
        super(partecipants, penalty, flightDayPenalty, reward, firePowerRequired, creditReward);
    }

    public void meteorStorm(Player player, Shield shieldActivated, Map<Projectile, Direction> projectile) {
        for(Map.Entry<Projectile,Direction> entry : projectile.entrySet()){

            if(entry.getValue()== shieldActivated.getOrientation().getFirst() ||entry.getValue()== shieldActivated.getOrientation().get(1)||){

            }else{
                if(entry.getValue()== Direction.UP||entry.getValue()== Direction.DOWN){
                    destroyComponent(player,index,line);
                }else{
                    destroyComponent(player,line, index);
                }
            }
        }
    }


    @Override
    public void play() {

    }
}
