package it.polimi.it.galaxytrucker.old;


import it.polimi.it.galaxytrucker.aventurecard.AdventureDeck;
import it.polimi.it.galaxytrucker.cardEffects.CrewmatePenalty;
import it.polimi.it.galaxytrucker.cardEffects.FlightDayPenalty;
import it.polimi.it.galaxytrucker.componenttiles.ComponentTile;
import it.polimi.it.galaxytrucker.componenttiles.Shield;
import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.utility.Direction;
import it.polimi.it.galaxytrucker.utility.Projectile;

import java.util.*;

public class CombatZone extends Attack implements FlightDayPenalty, CrewmatePenalty{
    private final int FLYPENALTY = 3;
    private final int CREWPENALTY = 2;


    public CombatZone(Optional partecipants, Optional penalty, Optional flightDayPenalty, Optional reward, int firePowerRequired, int creditReward, AdventureDeck adventureDeck, Map<Projectile, Direction> projectiles) {
        super(partecipants, penalty, flightDayPenalty, reward, firePowerRequired, creditReward,adventureDeck, projectiles);
    }


    @Override
    public void applyCrewmatePenalty(int penalty, Player player) {
        //super.getDeck().
    }

    @Override
    public void applyFlightDayPenalty(int penalty, Player player) {
        super.getDeck().getGameManager().getFlightBoardState().movePlayerBackwards(penalty, player.getPlayerID());
    }

    @Override
    public void play() {
        //controllo equipaggio
        List<Player> players = super.getPartecipants().stream().toList();
        Player temp;
        temp=players.getFirst();
        for (int i=1;i<players.size();i++) {
            if(players.get(i).getShipManager().calculateEnginePower()<temp.getShipManager().calculateEnginePower()) {
                temp = players.get(i+1);
            }
        }
        applyCrewmatePenalty(CREWPENALTY, temp);

        temp=players.getFirst();
        for (int i=1;i<players.size();i++) {
            if(players.get(i).getShipManager().countCrewmates()<temp.getShipManager().countCrewmates()) {
                temp = players.get(i+1);
            }
        }
        applyFlightDayPenalty(FLYPENALTY, temp);


        temp=players.getFirst();
        for (int i=1;i<players.size();i++) {
            if(players.get(i).getShipManager().calculateFirePower()<temp.getShipManager().calculateFirePower()) {
                temp = players.get(i+1);
            }
        }
        attack(temp);

    }

    @Override
    public void attack(Player player) {
        Scanner scanner = new Scanner(System.in);
        ComponentTile componetHit = null;
        List<Optional<ComponentTile>> sequence;
        int line,index=0,y=0;
        Map<Projectile, Direction> projectiles = super.getProjectiles();
        List<ComponentTile> listScudi = null;
        List<Shield> shield = null;



        for (Map.Entry<Projectile, Direction> entry : projectiles.entrySet()) {
            line=super.rollDice();


            //recupera la colonna/riga in cui c'è il proiettile
            if(entry.getValue()==Direction.UP||entry.getValue()==Direction.DOWN){
                //dato l'indice della colonna mi ritorna tutta la colonna
                sequence = player.getShipManager().getComponentsAtColumn(line);
            }else {
                //dato l'indice della riga mi ritorna tutta la riga
                sequence =  player.getShipManager().getComponentsAtRow(line);
            }

            //trovo il primo pezzo che verrà colpito

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
            //con le cannonate controllo subito gli scudi
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
                        if(s.getOrientation()==entry.getValue()||s.getOrientation()==){
                            if (activate){
                                s.activate();
                                y=1;
                            }
                        }
                    }


            if(y==0){
                if(entry.getValue()==Direction.UP||entry.getValue()==Direction.DOWN){
                    player.getShipManager().removeComponentTile(index,line);
                }else {
                    player.getShipManager().removeComponentTile(line,index);
                }
            }



            }else{//PROIETTILE GROSSO
                if(entry.getValue()==Direction.UP||entry.getValue()==Direction.DOWN){
                    player.getShipManager().removeComponentTile(index,line);
                }else {
                    player.getShipManager().removeComponentTile(line,index);
                }
            }
        }
    }

    @Override
    public void meteorStorm(Player player, int line, Map.Entry<Projectile, Direction> projectiles) {

    }


}
