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

public class CombatZone extends Attack implements FlightDayPenalty, CrewmatePenalty{
    private List<Player> players = super.getPartecipants().stream().toList();
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
       // super.getDeck().getGameManager().
    }

    @Override
    public void play() {



        //controllo equipaggio
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




    }

    @Override
    public void attack() {
        ComponentTile componetHit = null;
        List<Optional<ComponentTile>> sequence;
        int line,rotation,index=0;
        Map<Projectile, Direction> projectiles = super.getProjectiles();
        Player player;

        if(players.get(0).getShipManager().calculateFirePower(players.get(0).getPlayerID())< players.get(1).getShipManager().calculateEnginePower(players.get(1).getPlayerID())){
            player = players.getFirst();
        }else{
            player = players.get(1);
        }



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
                        shield.activate();
                    }
                    else{
                        //se il componente non ha il lato liscio che corrisponde alla direzione del proiettile subisce danno
                        if (componetHit.getTileEdges().get(rotation) != TileEdge.SMOOTH){
                            if(entry.getValue()==Direction.UP||entry.getValue()==Direction.DOWN){
                                removeComponent(index,line);
                            }else {
                                removeComponent(line,index);
                            }
                        }

                    }
                }
            }else{
                if(entry.getValue()==Direction.UP||entry.getValue()==Direction.DOWN){
                    removeComponent(index,line);
                }else {
                    removeComponent(line,index);
                }
            }
        }
    }



}
