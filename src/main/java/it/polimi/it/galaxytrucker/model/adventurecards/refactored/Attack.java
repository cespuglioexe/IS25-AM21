package it.polimi.it.galaxytrucker.model.adventurecards.refactored;

import java.util.HashMap;
import java.util.List;

import it.polimi.it.galaxytrucker.model.componenttiles.Shield;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.Dice;
import it.polimi.it.galaxytrucker.model.utility.Direction;
import it.polimi.it.galaxytrucker.model.utility.Projectile;

public abstract class Attack {
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

    public void aimAtCoordsWith(Projectile projectile) {
        int aimedRow = Dice.roll() + Dice.roll();
        int aimedColumn = Dice.roll() + Dice.roll();

        projectilesAndAimedComponent.put(projectile, List.of(aimedRow, aimedColumn));
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
    }

    public abstract void attack();
}
