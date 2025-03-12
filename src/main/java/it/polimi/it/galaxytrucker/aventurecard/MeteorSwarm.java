package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.utility.Direction;
import it.polimi.it.galaxytrucker.utility.Projectile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MeteorSwarm extends Attack {
    public MeteorSwarm(Optional partecipants, Optional penalty, Optional flightDayPenalty, Optional reward, int firePowerRequired, int creditReward, AdventureDeck deck, Map<Projectile, Direction> projectiles) {
        super(partecipants, penalty, flightDayPenalty, reward, firePowerRequired, creditReward, deck, projectiles);
    }

    private int line;

    @Override
    public void attack(Player player) {
        //trovo riga/colonna da colpire
        Map<Projectile, Direction> meteorite = getProjectiles();

        if()




//          else{
//            //se il componente non ha il lato liscio che corrisponde alla direzione del proiettile subisce danno
//            if (componetHit.getTileEdges().get(rotation) != TileEdge.SMOOTH){
//                if(entry.getValue()==Direction.UP||entry.getValue()==Direction.DOWN){
//                    player.getShipManager().removeComponentTile(index,line);
//                }else {
//                    player.getShipManager().removeComponentTile(line,index);
//                }
//            }
//
//        }
    }

    @Override
    public void play() {
        List<Player> partecipants = super.getPartecipants().stream().toList();
        Map<Projectile, Direction> meteorite = getProjectiles();


        for(Map.Entry<Projectile, Direction> entry : meteorite.entrySet()) {
            line=super.rollDice();

            for (Player p: partecipants){
                attack(p);
            }

        }


    }
}
