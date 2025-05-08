package it.polimi.it.galaxytrucker.model.adventurecards.refactored;

import java.util.HashMap;
import java.util.List;

import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.pirates.StartState;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCard;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.CreditReward;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.FlightDayPenalty;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.attack.Attack;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.attack.Projectile;
import it.polimi.it.galaxytrucker.model.design.strategyPattern.FlightRules;
import it.polimi.it.galaxytrucker.model.exceptions.IllegalComponentPositionException;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.Direction;
import it.polimi.it.galaxytrucker.model.utility.ProjectileType;

public class Pirates extends Attack implements AdventureCard,FlightDayPenalty, CreditReward{
    private int firePowerRequired;
    private int creditReward;
    private int flightDayPenalty;
    HashMap<Player,Double> playersAndFirePower;

    private final FlightRules flightRules;
    
    public Pirates(int firePowerRequired, int creditReward, int flightDayPenalty, List<Projectile> projectiles, FlightRules flightRules) {
        super(projectiles);
        this.creditReward = creditReward;
        this.flightDayPenalty = flightDayPenalty;
        this.flightRules = flightRules;
        playersAndFirePower = new HashMap<>();
    }
    
    @Override
    public void play() {
        start(new StartState());
    }


    public void nextPlayer() {
        List<Player> players = flightRules.getPlayerOrder();

        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).equals(getPlayer())) {
                if (i + 1 < players.size()) {
                    setPlayer(players.get(i + 1));
                    playersAndFirePower.put(players.get(i + 1),(double)super.getPlayerFirePower());
                    return;
                }
            }
        }
        setPlayer(players.getFirst());
        playersAndFirePower.put(players.getFirst(),(double)super.getPlayerFirePower());
    }

    public HashMap<Player,Double> getPlayersAndFirePower() {
        return this.playersAndFirePower;
    }

    public void selectCannons(HashMap<List<Integer>, List<Integer>> doubleCannonsAndBatteries) {
        ShipManager ship = super.getPlayer().getShipManager();
        double firePower = ship.activateComponent(doubleCannonsAndBatteries);

        double baseFirePower = playersAndFirePower.get(super.getPlayer());
        firePower += baseFirePower;

        playersAndFirePower.put(super.getPlayer(), firePower);
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
