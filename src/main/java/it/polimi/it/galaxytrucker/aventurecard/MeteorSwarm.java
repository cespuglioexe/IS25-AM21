package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.componenttiles.ComponentTile;
import it.polimi.it.galaxytrucker.componenttiles.Shield;
import it.polimi.it.galaxytrucker.componenttiles.TileEdge;
import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.utility.Direction;
import it.polimi.it.galaxytrucker.utility.Projectile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class MeteorSwarm extends Attack {
    public MeteorSwarm(Optional partecipants, Optional penalty, Optional flightDayPenalty, Optional reward, int firePowerRequired, int creditReward, AdventureDeck deck, Map<Projectile, Direction> projectiles) {
        super(partecipants, penalty, flightDayPenalty, reward, firePowerRequired, creditReward, deck, projectiles);
    }

    private int line;

    @Override
    public void attack(Player player) {

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
    public void meteorStorm(Player player, int line, Map.Entry<Projectile, Direction> entry) {
        List<Optional<ComponentTile>> sequence;
        ComponentTile componetHit = null;
        int index=0;
        List<ComponentTile> scudi = null;

        if(entry.getValue()==Direction.UP||entry.getValue()==Direction.DOWN){
            //dato l'indice della colonna mi ritorna tutta la colonna
            sequence = player.getShipManager().getComponentsAtColumn(line);
        }else {
            //dato l'indice della riga mi ritorna tutta la riga
            sequence =  player.getShipManager().getComponentsAtRow(line);
        }

        for (Optional<ComponentTile> c : sequence) {
            if(c.isPresent()){
                componetHit = c.get();
                break;
            }
            index++;
        }

        if(super.getDeck().getGameManager().getLevel()==1){
            index=index+4;
        }else{
            index=index+5;
        }

        if (entry.getKey()==Projectile.SMALL){
            if (componetHit.getTileEdges().getFirst() != TileEdge.SMOOTH){
                //cerco gli scudi
                Set<List<Integer>> posizioniScudi = player.getShipManager().getAllComponentsPositionOfType(Shield.class);
                for (List<Integer> liste:posizioniScudi) {
                    scudi = player.getShipManager().getComponent(liste.getFirst(),liste.get(1)).stream().toList();
                }

                //controllo se ci sono scudi orientati correttamente
                for(ComponentTile s: scudi){
                    if(s.)
                }


                if(entry.getValue()==Direction.UP||entry.getValue()==Direction.DOWN){
                    player.getShipManager().removeComponentTile(index,line);
                }else {
                    player.getShipManager().removeComponentTile(line,index);
                }
            }

        }else{

        }


    }

    @Override
    public void play() {
        List<Player> partecipants = super.getPartecipants().stream().toList();
        Map<Projectile, Direction> meteorite = getProjectiles();


        for(Map.Entry<Projectile, Direction> entry : meteorite.entrySet()) {
            line=super.rollDice();

            for (Player p: partecipants){
                meteorStorm(p, line, entry);
            }

        }


    }
}
