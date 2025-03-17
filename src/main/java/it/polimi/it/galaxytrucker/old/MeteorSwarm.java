package it.polimi.it.galaxytrucker.old;

import it.polimi.it.galaxytrucker.aventurecard.AdventureDeck;
import it.polimi.it.galaxytrucker.componenttiles.*;
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
        List<ComponentTile> listScudi = null;
        List<ComponentTile> listSingleCannon = null;
        List<ComponentTile> listDoubleCannon = null;

        Set<List<Integer>>  posizioneDoubleCannon = null;
        Set<List<Integer>> posizioneSingleCannon = null;
        Set<List<Integer>> posizioneCannons = null;


        List<Shield> shield = null;
        List<ComponentTile> singleCannons = null;
        List<DoubleCannon> doubleCannons = null;

        List<ComponentTile> activableCannons = null;
        int y=0;


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
                    listScudi = player.getShipManager().getComponent(liste.getFirst(),liste.get(1)).stream().toList();
                }
                //trasformo la lista di componenti in lista di scudi
                for (int i=0;i<listScudi.size();i++){
                    shield.add((Shield) listScudi.get(i));
                }


                //controllo se ci sono scudi orientati correttamente
                for(Shield s: shield){
                    if(s.getOrientation()==entry.getValue()||Direction.values()[(s.getOrientation().ordinal()+1)%4]==entry.getValue()){
                        if(activate){
                            s.activate();
                            y = 1;
                            break;
                        }
                    }
                }


                if (y==0){
                    if(entry.getValue()==Direction.UP||entry.getValue()==Direction.DOWN){
                        player.getShipManager().removeComponentTile(index,line);
                    }else {
                        player.getShipManager().removeComponentTile(line,index);
                    }
                }

            }

        }else{


            posizioneSingleCannon = player.getShipManager().getAllComponentsPositionOfType(SingleCannon.class);
            posizioneDoubleCannon = player.getShipManager().getAllComponentsPositionOfType(DoubleCannon.class);
            posizioneCannons.addAll(posizioneSingleCannon);
            posizioneCannons.addAll(posizioneDoubleCannon);


            //se il meteorite è grosso non ci si può difendere con gli scudi bisogna sparare
            posizioneSingleCannon = player.getShipManager().getAllComponentsPositionOfType(SingleCannon.class);

            for (List<Integer> liste:posizioneSingleCannon) {
                 listSingleCannon = player.getShipManager().getComponent(liste.getFirst(), liste.get(1)).stream().toList();
            }
            //trasformo la lista di componenti in lista di single cannons
            for (int i=0;i<listSingleCannon.size();i++){
                singleCannons.add((SingleCannon) listSingleCannon.get(i));
            }





            /*-----------------------------*/
            posizioneDoubleCannon = player.getShipManager().getAllComponentsPositionOfType(DoubleCannon.class);

            for (List<Integer> liste:posizioneDoubleCannon) {
                listDoubleCannon = player.getShipManager().getComponent(liste.getFirst(), liste.get(1)).stream().toList();
            }
            //trasformo la lista di componenti in lista di single cannons
            for (int i=0;i<listDoubleCannon.size();i++){
                doubleCannons.add((DoubleCannon) listDoubleCannon.get(i));
            }



            //lista cannoni attivabili
            for (int i=0; i<singleCannons.size();i++){
                if (Direction.values()[singleCannons.get(i).getRotation()]==entry.getValue()){
                    activableCannons.add(singleCannons.get(i));
                }
            }

            for (int i=0; i<doubleCannons.size();i++){
                if (Direction.values()[doubleCannons.get(i).getRotation()]==entry.getValue()){
                    activableCannons.add(doubleCannons.get(i));
                }
            }




            if(entry.getValue()==Direction.UP||entry.getValue()==Direction.DOWN){
                player.getShipManager().removeComponentTile(index,line);
            }else {
                player.getShipManager().removeComponentTile(line,index);
            }
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
