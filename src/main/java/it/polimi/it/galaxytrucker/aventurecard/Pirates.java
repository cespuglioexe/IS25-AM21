package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.cardEffects.CreditReward;
import it.polimi.it.galaxytrucker.cardEffects.FlightDayPenalty;
import it.polimi.it.galaxytrucker.componenttiles.ComponentTile;
import it.polimi.it.galaxytrucker.componenttiles.Shield;
import it.polimi.it.galaxytrucker.managers.FlightBoardState;
import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.utility.Direction;
import it.polimi.it.galaxytrucker.utility.Projectile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class Pirates extends AdventureCard implements FlightDayPenalty, CreditReward {
    private List<Optional<ComponentTile>> sequence;
    private int index, line;

    public Pirates(Optional penalty, Optional flightDayPenalty, Optional reward, int firePowerRequired, int creditReward) {
        super(penalty, flightDayPenalty, reward, firePowerRequired, creditReward);
    }


    public boolean checkIfPlayerLoseToPirates(Player player){
        return player.getShipManager().calculateFirePower() < super.getFirePowerRequired();
    }

    public void checkComponentHit(Player player, Map<Projectile,Direction> projectile, int lines) {
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
        int protect = 0;

        for(Map.Entry<Projectile,Direction> entry : projectile.entrySet()){
            for (Shield shield : shieldActivated) {
                if(entry.getValue()== shield.getOrientation().getFirst() ||entry.getValue()== shield.getOrientation().get(1) && entry.getKey() == Projectile.SMALL){
                    protect = 1;
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

    public void destroyComponent(Player player,int row, int col) {
        player.getShipManager().removeComponentTile(row, col);
    }




    @Override
    public void giveCreditReward(Player player) {
        player.addCredits(super.getCreditReward());
    }

    @Override
    public void applyFlightDayPenalty(FlightBoardState fs, Player player) {
        fs.movePlayerBackwards((int)getFlightDayPenalty().orElse(0), player.getPlayerID());
    }
}
