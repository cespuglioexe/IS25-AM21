package it.polimi.it.galaxytrucker.model.aventurecard.refactored;

import java.util.HashMap;
import java.util.List;

import it.polimi.it.galaxytrucker.model.aventurecard.interfaces.AdventureCard;
import it.polimi.it.galaxytrucker.model.aventurecard.interfaces.CreditReward;
import it.polimi.it.galaxytrucker.model.aventurecard.interfaces.FlightDayPenalty;
import it.polimi.it.galaxytrucker.model.design.strategyPattern.FlightRules;
import it.polimi.it.galaxytrucker.model.exceptions.IllegalComponentPositionException;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.Direction;
import it.polimi.it.galaxytrucker.model.utility.Projectile;

public class Pirates extends Attack implements AdventureCard,FlightDayPenalty, CreditReward{
    private int firePowerRequired;
    private int creditReward;
    private int flightDayPenalty;

    private final FlightRules flightRules;
    
    public Pirates(int firePowerRequired, int creditReward, int flightDayPenalty, HashMap<Projectile, Direction> projectilesAndDirections, FlightRules flightRules) {
        super(projectilesAndDirections); 
        this.creditReward = creditReward;
        this.flightDayPenalty = flightDayPenalty;
        this.flightRules = flightRules;
    }
    
    @Override
    public void play() {
        nextPlayer();

        for (Projectile projectile : getProjectilesAndDirection().keySet()) {
            aimAtCoordsWith(projectile);
        }
    }
    private void nextPlayer() {
        List<Player> players = flightRules.getPlayerOrder();

        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).equals(getPlayer())) {
                if (i + 1 < players.size()) {
                    setPlayer(players.get(i + 1));
                    return;
                }
                //TODO NEXT STATE
            }
        }
        setPlayer(players.get(0));
    }

    @Override
    public void attack() {
        if (getPlayerFirePower() > firePowerRequired) {
            return;
        }
       
        for (Projectile projectile : getProjectilesAndDirection().keySet()) {
            List<Integer> aimedCoords = getAimedCoordsByProjectile(projectile);
            Direction direction = getProjectilesAndDirection().get(projectile);

            if (projectile == Projectile.SMALL) {
                if (isShieldActivated(direction)) {
                    continue;
                }
            }
            destroyComponent(aimedCoords.get(0), aimedCoords.get(1));
        }
        nextPlayer();
    }
    private boolean isShieldActivated(Direction direction) {
        for (List<Integer> shieldCoord : getShieledsAndDirection().keySet()) {
            List<Direction> coveredDirections = getShieledsAndDirection().get(shieldCoord);
            if (coveredDirections.contains(direction)) {
                return true;
            }
        }
        return false;
    }
    private void destroyComponent(int row, int column) {
        ShipManager ship = getPlayer().getShipManager();
        try {
            ship.removeComponentTile(row, column);
        } catch (IllegalComponentPositionException emptyTile) {

        } catch (IndexOutOfBoundsException missedShot) {

        }
    }
    
    @Override
    public int getCreditReward() {
        return creditReward;
    }

    @Override
    public void applyCreditReward() {
        getPlayer().addCredits(creditReward);
    }
    
    @Override
    public void applyFlightDayPenalty() {
        flightRules.movePlayerBackwards(flightDayPenalty, getPlayer());
    }
}
