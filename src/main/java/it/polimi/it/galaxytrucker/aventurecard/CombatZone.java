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
       // super.getDeck().getGameManager().movePlayerBackwords(int, int)
    }

    @Override
    public void play() {
        //controllo equipaggio
        List<Player> players = super.getPartecipants().stream().toList();
        Player temp;
        for (int i=0;i<players.size();i++) {
            if(players.get(i).getShipManager().countCrewmates()<players.get(i+1).getShipManager().countCrewmates()){
                temp = players.get(i);
            }
        }

        /*
        if(players.get(0).getShipManager().calculateCrewmates(players.get(0).getPlayerID())< players.get(1).getShipManager().calculateCrewmates(players.get(1).getPlayerID())){
            applyFlightDayPenalty(FLYPENALTY, players.get(0));
        }else{
            applyFlightDayPenalty(FLYPENALTY, players.get(1));
        }

        //controllo potenza motrice
        if(players.get(0).getShipManager().calculateEnginePower(players.get(0).getPlayerID())< players.get(1).getShipManager().calculateEnginePower(players.get(1).getPlayerID())){
            applyCrewmatePenalty(CREWPENALTY, players.get(0));
        }else{
            applyCrewmatePenalty(CREWPENALTY,players.get(1));
        }

        if(player.getShipManager().calculateFirePower(players.get(0).getPlayerID())< players.get(1).getShipManager().calculateEnginePower(players.get(1).getPlayerID())){
            player = player.getFirst();
        }else{
            player = players.get(1);
        }
        */


    }

    @Override
    public void attack(Player player) {
        Scanner scanner = new Scanner(System.in);
        ComponentTile componetHit = null;
        List<Optional<ComponentTile>> sequence;
        int line,rotation,index=0;
        Map<Projectile, Direction> projectiles = super.getProjectiles();




        for (Map.Entry<Projectile, Direction> entry : projectiles.entrySet()) {
            line=super.rollDice();

            if (entry.getValue()==Direction.UP){
                rotation = 0;
            }
            if (entry.getValue()==Direction.DOWN){
                rotation = 1;
            }
            if (entry.getValue()==Direction.LEFT){
                rotation = 2;
            }
            if (entry.getValue()==Direction.RIGHT){
                rotation = 3;
            }

            //recupera la colonna/riga in cui c'è il proiettile
            if(entry.getValue()==Direction.UP||entry.getValue()==Direction.DOWN){
                //dato l'indice della colonna mi ritorna tutta la colonna
                sequence = player.getShipManager().getColumn(line);
            }else {
                //dato l'indice della riga mi ritorna tutta la riga
                sequence =  player.getShipManager().getRow(line);
            }

            //trovo il primo pezzo che verrà colpito

            for (Optional<ComponentTile> c : sequence) {
                if(c.isPresent()){
                    componetHit = c.get();
                    break;
                }
                index++;
            }

            if (entry.getKey()==Projectile.SMALL){
                if(/*Controlla se c'è lo scudo girato nella direzione giusta altrimenti danno*/){
                    //trova lo scudo e chiede se attivarlo
                    System.out.println("Activate shield? 1 = yes");
                    if (scanner.nextInt() == 1){
                        player.getShipManager().activateShield();
                    }
                    else{
                        //se il componente non ha il lato liscio che corrisponde alla direzione del proiettile subisce danno
                        if (componetHit.getTileEdges().get(rotation) != TileEdge.SMOOTH){
                            if(entry.getValue()==Direction.UP||entry.getValue()==Direction.DOWN){
                                player.getShipManager().removeComponentTile(index,line);
                            }else {
                                player.getShipManager().removeComponentTile(line,index);
                            }
                        }

                    }
                }
            }else{
                if(entry.getValue()==Direction.UP||entry.getValue()==Direction.DOWN){
                    player.getShipManager().removeComponentTile(index,line);
                }else {
                    player.getShipManager().removeComponentTile(line,index);
                }
            }
        }
    }



}
