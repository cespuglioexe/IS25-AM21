package it.polimi.it.galaxytrucker.model.adventurecards.cards;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.meteorswarm.StartState;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCard;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.attack.Attack;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.attack.Projectile;
import it.polimi.it.galaxytrucker.model.componenttiles.SingleCannon;
import it.polimi.it.galaxytrucker.model.design.strategyPattern.FlightRules;
import it.polimi.it.galaxytrucker.exceptions.IllegalComponentPositionException;
import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.Direction;
import it.polimi.it.galaxytrucker.model.utility.ProjectileType;

public class MeteorSwarm extends Attack implements AdventureCard {
    private Projectile currentMeteor;
    private final FlightRules flightRules;
    private boolean shootingAtMeteor = false;

    public MeteorSwarm(List<Projectile> projectiles, FlightRules flightRules) {
        super(projectiles);
        this.flightRules = flightRules;
    }

    @Override
    public void play() {
        start(new StartState());
    }

    public Optional<Player> nextPlayer() {
        List<Player> players = flightRules.getPlayerOrder();
        Optional<Player> player;

        if (playerNotSet()) {
            player = Optional.of(players.get(0));
            setPlayer(player.get());
            return player;
        }
    
        int index = players.indexOf(getPlayer());
        if (index != -1 && index < players.size() - 1) {
            player = Optional.of(players.get(index + 1));
            setPlayer(player.get());
            return player;
        }
    
        return Optional.empty();
    }
    private boolean playerNotSet() {
        if (Optional.ofNullable(getPlayer()).isEmpty()) {
            return true;
        }
        return false;
    }

    public Optional<Projectile> nextMeteor(){
        List<Projectile> meteors = super.getProjectiles().stream().toList();
        Optional<Projectile> meteor;

        if (meterNotSet()) {
            meteor = Optional.of(meteors.get(0));
            currentMeteor = meteor.get();
            return meteor;
        }

        int index = meteors.indexOf(currentMeteor);
        if (index != -1 && index < meteors.size() - 1) {
            meteor = Optional.of(meteors.get(index + 1));
            currentMeteor = meteor.get();
            return meteor;
        }

        currentMeteor = null;
        return Optional.empty();
    }
    private boolean meterNotSet() {
        if (Optional.ofNullable(currentMeteor).isEmpty()) {
            return true;
        }
        return false;
    }

    public Projectile getCurrentMeteor() {
        return currentMeteor;
    }

    @Override
    public void attack() {
        List<Integer> aimedCoords = getAimedCoordsByProjectile(currentMeteor);
        Direction direction = currentMeteor.getDirection();

        if (currentMeteor.getSize() == ProjectileType.SMALL) {
            if (isShieldActivated(direction)) {
                return;
            }
            if (hasExposedConnector(aimedCoords.get(0), aimedCoords.get(1), direction)) {
                destroyComponent(aimedCoords.get(0), aimedCoords.get(1));
            }
            return;
        } else {
            if (!shootingAtMeteor) {
                destroyComponent(aimedCoords.get(0), aimedCoords.get(1));
            } else {
                shootingAtMeteor = false;
            }
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
    private boolean hasExposedConnector(int row, int column, Direction direction) {
        ShipManager ship = getPlayer().getShipManager();
        
        try {
            return ship.hasExposedConnectorAtDirection(row, column, direction.reverse());
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    public void shootAtMeteorWith(List<Integer> cannonCoord) {
        ShipManager ship = getPlayer().getShipManager();

        SingleCannon cannon = (SingleCannon) ship.getComponent(cannonCoord.get(0), cannonCoord.get(1)).get();
        Direction cannonDirection = Direction.fromInt(cannon.getRotation());
        List<Integer> aimedCoords = getAimedCoordsByProjectile(currentMeteor);

        if (!isFacingTheMeteor(cannonDirection)) {
            throw new InvalidActionException("The cannon must face the meteor");
        }

        if (!isInTheSameDirection(cannonCoord, aimedCoords)) {
            throw new InvalidActionException("The cannon must face the meteor");
        }

        shootingAtMeteor = true;
        updateState();
    }
    private boolean isFacingTheMeteor(Direction direction) {
        Direction meteorDirection = currentMeteor.getDirection();

        if (direction == meteorDirection.reverse()) {
            return true;
        }
        return false;
    }
    private boolean isInTheSameDirection(List<Integer> cannonCoord, List<Integer> meteorCoord) {
        Direction meteorDirection = currentMeteor.getDirection();

        if (meteorDirection == Direction.DOWN || meteorDirection == Direction.UP) {
            if (cannonCoord.get(1) == meteorCoord.get(1)) {
                return true;
            }
            return false;
        }
        if (cannonCoord.get(0) == meteorCoord.get(0)) {
            return true;
        }
        return false;
    }

    public void notShootAtMeteor() {
        updateState();
    }

    public void shootAtMeteorWith(List<Integer> doubleCannonCoord, List<Integer> batteryCoord) {
        ShipManager ship = getPlayer().getShipManager();
        HashMap<List<Integer>, List<Integer>> doubleCannonAndBattery = new HashMap<>();
        doubleCannonAndBattery.put(doubleCannonCoord, batteryCoord);

        ship.activateComponent(doubleCannonAndBattery);

        shootAtMeteorWith(doubleCannonCoord);
    }

}
