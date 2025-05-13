package it.polimi.it.galaxytrucker.model.adventurecards.refactored;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.pirates.StartState;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCard;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.CreditReward;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.FlightDayPenalty;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.attack.Attack;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.attack.Projectile;
import it.polimi.it.galaxytrucker.model.design.strategyPattern.FlightRules;
import it.polimi.it.galaxytrucker.exceptions.IllegalComponentPositionException;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.Direction;
import it.polimi.it.galaxytrucker.model.utility.ProjectileType;

public class Pirates extends Attack implements AdventureCard,FlightDayPenalty, CreditReward{
    private int firePowerRequired;
    private int creditReward;
    private int flightDayPenalty;
    LinkedHashMap<Player,Double> playersAndFirePower;

    private final FlightRules flightRules;
    
    public Pirates(int firePowerRequired, int creditReward, int flightDayPenalty, List<Projectile> projectiles, FlightRules flightRules) {
        super(projectiles);
        this.creditReward = creditReward;
        this.flightDayPenalty = flightDayPenalty;
        this.flightRules = flightRules;
        this.firePowerRequired = firePowerRequired;
        playersAndFirePower = new LinkedHashMap<>();
    }
    
    @Override
    public void play() {
        start(new StartState());
    }


    public void selectPlayer(){
        List<Player> players = flightRules.getPlayerOrder();
        if(super.getPlayer() == null){
            super.setPlayer(players.getFirst());
            return;
        }
        super.setPlayer(nextPlayer(players).orElse(null));
    }

    private Optional<Player> nextPlayer(List<Player> players) {
        for(int i=0;i<players.size();i++){
            if(players.get(i).equals(getPlayer()) && (i+1) < players.size()) {
                return Optional.of(players.get(i + 1));
            }
        }
        return Optional.empty();
    }

    public LinkedHashMap<Player,Double> getPlayersAndFirePower() {
        return this.playersAndFirePower;
    }

    public void selectCannons(HashMap<List<Integer>, List<Integer>> doubleCannonsAndBatteries) {
        ShipManager ship = super.getPlayer().getShipManager();
        double firePower = ship.activateComponent(doubleCannonsAndBatteries);

        double baseFirePower = super.getPlayerFirePower();
        firePower += baseFirePower;

        playersAndFirePower.put(super.getPlayer(),firePower);
        updateState();
    }

    public void selectNoCannons() {
        playersAndFirePower.put(super.getPlayer(),(double)super.getPlayerFirePower());
        updateState();
    }

    @Override
    public void attack() {
        for (Projectile projectile : super.getProjectiles()) {
            List<Integer> aimedCoords = getAimedCoordsByProjectile(projectile);
            Direction direction = projectile.getDirection();

            if (projectile.getSize() == ProjectileType.SMALL) {
                if (isShieldActivated(direction)) {
                    continue;
                }
            }
            destroyComponent(aimedCoords.get(0), aimedCoords.get(1));
        }
    }

    private void destroyComponent(int row, int column) {
        ShipManager ship = getPlayer().getShipManager();
        try {
            ship.removeComponentTile(row, column);
        } catch (IllegalComponentPositionException emptyTile) {

        } catch (IndexOutOfBoundsException missedShot) {

        }
    }

    public int getNumberOfBoardPlayers() {
        return flightRules.getPlayerOrder().size();
    }
    
    @Override
    public int getCreditReward() {
        return creditReward;
    }

    public int getFirePowerRequired() {
        return firePowerRequired;
    }

    @Override
    public void applyCreditReward() {
        getPlayer().addCredits(creditReward);
        updateState();
    }

    public void discardCreditReward(){
        creditReward = 0;
        flightDayPenalty = 0;
        updateState();
    }
    
    @Override
    public void applyFlightDayPenalty() {
        flightRules.movePlayerBackwards(flightDayPenalty, getPlayer());
    }
}
