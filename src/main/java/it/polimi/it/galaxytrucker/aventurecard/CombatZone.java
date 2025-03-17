package it.polimi.it.galaxytrucker.aventurecard;


import it.polimi.it.galaxytrucker.cardEffects.CrewmatePenalty;
import it.polimi.it.galaxytrucker.cardEffects.FlightDayPenalty;
import it.polimi.it.galaxytrucker.componenttiles.ComponentTile;
import it.polimi.it.galaxytrucker.componenttiles.Shield;
import it.polimi.it.galaxytrucker.managers.FlightBoardState;
import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.utility.Direction;
import it.polimi.it.galaxytrucker.utility.Projectile;

import java.util.*;

public class CombatZone extends AdventureCard implements FlightDayPenalty, CrewmatePenalty{
    private final int FLYPENALTY = 3;
    private final int CREWPENALTY = 2;;

    private int index,line;

    public CombatZone(Optional penalty, Optional flightDayPenalty, Optional reward, int firePowerRequired, int creditReward) {
        super(penalty, flightDayPenalty, reward, firePowerRequired, creditReward);
    }


    public Player checkLoserEp(List<Player> players) {
        Player loserEp = players.get(0);
        for (int i = 1; i < players.size()-1; i++) {
            if (loserEp.getShipManager().calculateEnginePower() > players.get(i).getShipManager().calculateEnginePower()) {
                loserEp = players.get(i);
            }
        }


        return loserEp;
    }

    public Player checkLoserFp(List<Player> players) {
        Player loserFp = players.get(0);

        for (int i = 1; i < players.size()-1; i++) {
            if (loserFp.getShipManager().calculateFirePower() > players.get(i).getShipManager().calculateFirePower()) {
                loserFp = players.get(i);
            }
        }

        return loserFp;
    }

    public Player checkLoserCm(List<Player> players) {
        Player loserCm = players.get(0);
        List<Player> playerList;

        for (int i = 1; i < players.size()-1; i++) {
            if(players.get(i).getShipManager().countCrewmates() < loserCm.getShipManager().countCrewmates()) {
                loserCm = players.get(i);
            }
        }

        return loserCm;
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

    public void attack(Player player, List<Shield> shieldActivated, Map<Projectile,Direction> projectile) {
        int protect =0;

        for(Map.Entry<Projectile,Direction> entry : projectile.entrySet()) {
            for (Shield shield : shieldActivated) {
                if (entry.getValue() == shield.getOrientation().getFirst() || entry.getValue() == shield.getOrientation().get(1) && entry.getKey() == Projectile.SMALL) {
                    protect =1;
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

    }

    public void destroyComponent(Player player, int row, int col) {
         player.getShipManager().removeComponentTile(row,col);
    }


    @Override
    public void applyCrewmatePenalty(int penalty, Player player) {
        player.getShipManager();
    }

    @Override
    public void applyFlightDayPenalty(FlightBoardState fs, Player player) {
        fs.movePlayerBackwards(FLYPENALTY,player.getPlayerID());
    }

}
