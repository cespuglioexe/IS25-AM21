package it.polimi.it.galaxytrucker.model.adventurecards.interfaces.attack;

import java.util.HashMap;
import java.util.List;

import it.polimi.it.galaxytrucker.model.componenttiles.Shield;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.Direction;
import it.polimi.it.galaxytrucker.model.utility.Projectile;

public abstract class Attack extends StateMachine {
    private Player player;
    private int playerFirePower;
    private HashMap<List<Integer>, List<Direction>> shieldsAndDirection;
    private HashMap<Projectile, Direction> projectilesAndDirection;
    private HashMap<Projectile, List<Integer>> projectilesAndAimedComponent;

    public Attack(HashMap<Projectile, Direction> projectilesAndDirection) {
        this.projectilesAndDirection = projectilesAndDirection;

        projectilesAndAimedComponent = new HashMap<>();
        for (Projectile projectile : projectilesAndDirection.keySet()) {
            projectilesAndAimedComponent.put(projectile, List.of());
        }
        shieldsAndDirection = new HashMap<>();
    }

    public Player getPlayer() {
        return player;
    }

    public int getPlayerFirePower() {
        return playerFirePower;
    }

    public HashMap<List<Integer>, List<Direction>> getShieledsAndDirection() {
        return shieldsAndDirection;
    }

    public HashMap<Projectile, Direction> getProjectilesAndDirection() {
        return projectilesAndDirection;
    }

    public List<Integer> getAimedCoordsByProjectile(Projectile projectile) {
        return projectilesAndAimedComponent.get(projectile);
    }

    public void setPlayer(Player player) {
        this.player = player;

        ShipManager ship = player.getShipManager();
        playerFirePower = (int) ship.calculateFirePower();
    }

    public List<Integer> aimAtCoordsWith(Projectile projectile) {
        Direction direction = projectilesAndDirection.get(projectile);

        List<Integer> aimedCoords = AimingSystem.aimFrom(direction, player.getShipManager());
        int aimedRow = aimedCoords.get(0);
        int aimedColumn = aimedCoords.get(1);

        projectilesAndAimedComponent.put(projectile, List.of(aimedRow, aimedColumn));
        return aimedCoords;
    }

    public void activateCannons(HashMap<List<Integer>, List<Integer>> doubleCannonsAndBatteries) {
        ShipManager ship = player.getShipManager();
        playerFirePower += ship.activateComponent(doubleCannonsAndBatteries);
    }

    public void activateShields(HashMap<List<Integer>, List<Integer>> shieldsAndBatteries) {
        ShipManager ship = player.getShipManager();
        ship.activateComponent(shieldsAndBatteries);

        for (List<Integer> shieldCoord : shieldsAndBatteries.keySet()) {
            Shield shield = (Shield) ship.getComponent(shieldCoord.get(0), shieldCoord.get(1)).get();

            shieldsAndDirection.put(shieldCoord, shield.getOrientation());
        }
        updateState();
    }

    public void activateNoShield() {
        updateState();
    }

    public boolean isShieldActivated(Direction direction) {
        for (List<Integer> shieldCoord : getShieledsAndDirection().keySet()) {
            List<Direction> coveredDirections = getShieledsAndDirection().get(shieldCoord);
            if (coveredDirections.contains(direction.reverse())) {
                return true;
            }
        }
        return false;
    }

    public abstract void attack();
}
