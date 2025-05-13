package it.polimi.it.galaxytrucker.model.adventurecards.interfaces.attack;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import it.polimi.it.galaxytrucker.model.componenttiles.Shield;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.Direction;
/**
 * Abstract base class for all adventure cards that involve projectile-based attacks in Galaxy Trucker.
 * <p>
 * This class manages shared functionality such as targeting, shield activation,
 * firepower management, and projectile direction tracking. It serves as the foundation
 * for concrete attack cards (e.g., CombatZone, Slavestation, etc.), each implementing
 * their own {@link #attack()} logic.
 *
 * <ul>
 *   <li>Stores the player being attacked and their current firepower.</li>
 *   <li>Tracks all projectiles and their directions.</li>
 *   <li>Uses the {@link AimingSystem} to determine impact coordinates for each projectile.</li>
 *   <li>Handles shield activation and determines whether incoming projectiles can be blocked.</li>
 *   <li>Stores the final aimed coordinates of each projectile for use during damage resolution.</li>
 * </ul>
 *
 * Each projectile has:
 * <ul>
 *   <li>A direction it travels (e.g., from the rear of the ship).</li>
 *   <li>An associated target coordinate.</li>
 *   <li>An optional shield that can block it, depending on orientation.</li>
 * </ul>
 *
 * The Shields system:
 * <ul>
 *   <li>Shields are activated using a map of shield coordinates and their battery inputs.</li>
 *   <li>After activation, each shield's orientation is stored to check if it can block a projectile.</li>
 *   <li>The method {@link #isShieldActivated(Direction)} checks if any shield is active in the reverse direction of an attack.</li>
 * </ul>
 *
 * <h2>Usage</h2>
 * Concrete classes must implement the {@link #attack()} method, which defines how and when projectiles are resolved.
 *
 * @author Stefano Carletto
 * @version 1.0
 *
 * @see StateMachine
 */
public abstract class Attack extends StateMachine {
    private Player player;
    private int playerFirePower;
    private HashMap<List<Integer>, List<Direction>> shieldsAndDirection;
    private LinkedHashMap<Projectile, List<Integer>> projectilesAndAimedComponent;

    public Attack(List<Projectile> projectiles) {
        projectilesAndAimedComponent = new LinkedHashMap<>();
        for (Projectile projectile : projectiles) {
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

    public Set<Projectile> getProjectiles() {
        return projectilesAndAimedComponent.keySet();
    }

    public List<Integer> getAimedCoordsByProjectile(Projectile projectile) {
        return projectilesAndAimedComponent.get(projectile);
    }

    public void setPlayer(Player player) {
        if(player!=null) {
            this.player = player;
            ShipManager ship = player.getShipManager();
            playerFirePower = (int) ship.calculateFirePower();
        }
    }

    /**
     * Determines the coordinates on the player's ship targeted by the given projectile.
     *
     * @param projectile the projectile being aimed (e.g., small or big shot)
     * @return the list of two integers representing the aimed row and column
     */
    public List<Integer> aimAtCoordsWith(Projectile projectile) {
        if (!projectilesAndAimedComponent.containsKey(projectile)) {
            throw new InvalidActionException("Cannot aim with projectile that is not in the arsenal");
        }
        Direction direction = projectile.getDirection();

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

    /**
     * Activates the specified shields on the player's ship by consuming battery power,
     * and registers their active orientation for use during projectile defense.
     * <p>
     * Each entry in the provided map contains the coordinates of a shield component
     * and a list of battery slots used to power it. After activation,
     * the shield's orientation is stored to determine
     * if it can block incoming projectiles during the {@code attack()} phase.
     *
     * @param shieldsAndBatteries a map where keys are the coordinates of shield components,
     *                            and values are lists of battery coordinates used to power them
     */
    public void activateShields(HashMap<List<Integer>, List<Integer>> shieldsAndBatteries) {
        ShipManager ship = player.getShipManager();
        System.out.println(player.getPlayerName());
        ship.activateComponent(shieldsAndBatteries);

        for (List<Integer> shieldCoord : shieldsAndBatteries.keySet()) {
            Shield shield = (Shield) ship.getComponent(shieldCoord.get(0), shieldCoord.get(1)).get();

            shieldsAndDirection.put(shieldCoord, shield.getOrientation());
        }
        updateState();
    }

    /**
     * Indicates that the player chooses not to activate any shields during the current attack phase.
     */
    public void activateNoShield() {
        updateState();
    }

    /**
     * Checks whether any active shield is oriented to block a projectile coming from the specified direction.
     * <p>
     * The method iterates over all active shields and their orientations,
     * and returns {@code true} if at least one shield covers the opposite direction of the attack.
     *
     * @param direction the direction from which the projectile is coming
     * @return {@code true} if a shield is active in that direction, {@code false} otherwise
     */
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
