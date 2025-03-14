package it.polimi.it.galaxytrucker.aventurecard;


import it.polimi.it.galaxytrucker.cardEffects.CrewmatePenalty;
import it.polimi.it.galaxytrucker.cardEffects.FlightDayPenalty;
import it.polimi.it.galaxytrucker.componenttiles.ComponentTile;
import it.polimi.it.galaxytrucker.componenttiles.TileEdge;
import it.polimi.it.galaxytrucker.crewmates.Crewmate;
import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.utility.Direction;
import it.polimi.it.galaxytrucker.utility.Projectile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class CombatZone extends Attack implements FlightDayPenalty, CrewmatePenalty{
    private final int FLYPENALTY = 3;
    private final int CREWPENALTY = 2;


    public CombatZone(Optional partecipants, Optional penalty, Optional flightDayPenalty, Optional reward, int firePowerRequired, int creditReward, AdventureDeck adventureDeck, Map<Projectile, Direction> projectiles) {
        super(partecipants, penalty, flightDayPenalty, reward, firePowerRequired, creditReward,adventureDeck, projectiles);
    }


    @Override
    public void applyCrewmatePenalty(int penalty, Player player) {
       // super.getDeck().getGameManager().
    }

    @Override
    public void applyFlightDayPenalty(int penalty, Player player) {
        super.getDeck().getGameManager().
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
        int line,index=0;
        Map<Projectile, Direction> projectiles = super.getProjectiles();

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
                if(/*Controlla se c'è lo scudo girato nella direzione giusta altrimenti danno*/){
                    //trova lo scudo e chiede se attivarlo
                    System.out.println("Activate shield? 1 = yes");
                    if (scanner.nextInt() == 1){
                        player.getShipManager().activateShield();
                    }
                    else{
                        if(entry.getValue()==Direction.UP||entry.getValue()==Direction.DOWN){
                            player.getShipManager().removeComponentTile(index,line);
                        }else {
                            player.getShipManager().removeComponentTile(line,index);
                        }
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
