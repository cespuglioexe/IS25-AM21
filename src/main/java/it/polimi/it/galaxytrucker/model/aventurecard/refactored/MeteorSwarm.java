package it.polimi.it.galaxytrucker.model.aventurecard.refactored;

import java.util.HashMap;
import java.util.List;

import it.polimi.it.galaxytrucker.model.aventurecard.interfaces.AdventureCard;
import it.polimi.it.galaxytrucker.model.componenttiles.SingleCannon;
import it.polimi.it.galaxytrucker.model.design.strategyPattern.FlightRules;
import it.polimi.it.galaxytrucker.model.exceptions.IllegalComponentPositionException;
import it.polimi.it.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.Direction;
import it.polimi.it.galaxytrucker.model.utility.Projectile;

public class MeteorSwarm extends Attack implements AdventureCard {
    private Projectile currentMeteor;
    private final FlightRules flightRules;

    public MeteorSwarm(HashMap<Projectile, Direction> meteorsAndDirections, FlightRules flightRules) {
        super(meteorsAndDirections);
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
                break;
            }
        }
        setPlayer(players.get(0));
    }

    @Override
    public void attack() {
        List<Integer> aimedCoords = getAimedCoordsByProjectile(currentMeteor);
        Direction direction = getProjectilesAndDirection().get(currentMeteor);

        if (currentMeteor == Projectile.SMALL) {
            if (isShieldActivated(direction)) {
                nextPlayer();
                return;
            }
            if (hasExposedConnector(aimedCoords.get(0), aimedCoords.get(1), direction)) {
                destroyComponent(aimedCoords.get(0), aimedCoords.get(1));
            }
            nextPlayer();
            return;
        }
        destroyComponent(aimedCoords.get(0), aimedCoords.get(1));
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
    private boolean hasExposedConnector(int row, int column, Direction direction) {
        ShipManager ship = getPlayer().getShipManager();
        
        return ship.hasExposedConnectorAtDirection(row, column, direction.reverse());
    }

    public void shootAtMeteorWith(List<Integer> cannonCoord) {
        ShipManager ship = getPlayer().getShipManager();

        SingleCannon cannon = (SingleCannon) ship.getComponent(cannonCoord.get(0), cannonCoord.get(1)).get();
        Direction cannonDirection = Direction.fromInt(cannon.getRotation());
        Direction meteorDirection = getProjectilesAndDirection().get(currentMeteor);
        List<Integer> aimedCoords = getAimedCoordsByProjectile(currentMeteor);

        if (!isFacingTheMeteor(cannonDirection)) {
            throw new InvalidActionException("The cannon must face the meteor");
        }

        if (meteorDirection == Direction.DOWN) {
            int aimedColumn = aimedCoords.get(1);
            if (cannonCoord.get(1) != aimedColumn) {
                throw new InvalidActionException("The cannon must be in the same column of the meteor");
            }
        } else {
            int aimedColumn = aimedCoords.get(1);
            int minCannonColumn = aimedColumn - 1;
            int maxCannonColumn = aimedColumn + 1;
            if (cannonCoord.get(1) < minCannonColumn || cannonCoord.get(1) > maxCannonColumn) {
                throw new InvalidActionException("The cannon must be in the range of one column from the meteor");
            }
        }

        nextPlayer();
    }
    private boolean isFacingTheMeteor(Direction direction) {
        Direction meteorDirection = getProjectilesAndDirection().get(currentMeteor);

        if (direction == meteorDirection.reverse()) {
            return true;
        }
        return false;
    }
    public void shootAtMeteorWith(List<Integer> doubleCannonCoord, List<Integer> batteryCoord) {
        ShipManager ship = getPlayer().getShipManager();
        HashMap<List<Integer>, List<Integer>> doubleCannonAndBattery = new HashMap<>();
        doubleCannonAndBattery.put(doubleCannonCoord, batteryCoord);

        ship.activateComponent(doubleCannonAndBattery);
        shootAtMeteorWith(doubleCannonCoord);
    }
}
