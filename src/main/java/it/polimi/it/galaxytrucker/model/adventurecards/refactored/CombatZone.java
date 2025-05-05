package it.polimi.it.galaxytrucker.model.adventurecards.refactored;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.combatZone.StartState;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCard;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.CrewmatePenalty;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.FlightDayPenalty;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.attack.Attack;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.model.design.strategyPattern.FlightRules;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.Direction;
import it.polimi.it.galaxytrucker.model.utility.Projectile;
import it.polimi.it.galaxytrucker.model.exceptions.IllegalComponentPositionException;
import it.polimi.it.galaxytrucker.model.exceptions.NotFoundException;

/**
 * Represents the "Combat Zone" adventure card in the game Galaxy Trucker.
 * <p>
 * It is implemented as a Finite State Machine (FSM),
 * transitioning through several internal states that model the card's phases.
 * <p>
 * In the event of ties, the player furthest ahead in flight order is affected.
 *
 * The card evaluates the following three criteria, one after the other:
 * <ol>
 *   <li><b>Crew Size Test:</b> The player with the fewest total crewmates (humans + aliens)
 *       loses 3 flight days (is moved backwards by 3 spaces on the flight board).</li>
 *   <li><b>Engine Power Test:</b> In flight order, players may choose to activate double engines
 *       by spending battery tokens. The player with the lowest engine power (after activations)
 *       must remove 2 crewmates from their ship.</li>
 *   <li><b>Firepower Test:</b> Again in flight order, players may activate double cannons
 *       by spending batteries. The player with the lowest firepower is targeted by two projectiles:
 *       <ul>
 *         <li>One small shot and one big shot, both fired from behind (upward direction on the ship).</li>
 *         <li>The small shot can be blocked using an active shield in the correct direction
 *             (at the cost of one battery). If not blocked, it destroys the hit component.</li>
 *         <li>The big shot cannot be blocked. It always destroys the component it hits,
 *             unless the shot misses the ship entirely based on the dice roll.</li>
 *       </ul>
 *   </li>
 * </ol>
 *
 * The card is implemented as a Finite State Machine with the following states:
 * <pre>
 * StartState
 *     ↓
 * FlightDayPenaltyState
 *     ↓
 * EngineSelectionState
 *     ↓
 * CrewmatePenaltyState
 *     ↓
 * CannonSelectionState
 *     ↓
 * AttackState
 *     ↓
 * EndState
 * </pre>
 * Each state encapsulates its own behavior and triggers a transition to the next
 * state upon completion.
 *
 * <h2>Implementation Notes</h2>
 * <ul>
 *   <li>This class extends {@link Attack} and uses its projectile-based attack logic.</li>
 *   <li>It implements {@link AdventureCard}, {@link FlightDayPenalty}, and {@link CrewmatePenalty}.</li>
 * </ul>
 * 
 * @author Stefano Carletto
 * @version 1.0
 *
 * @see AdventureCard
 * @see Attack
 * @see FlightDayPenalty
 * @see CrewmatePenalty
 * @see StateMachine
 */
public class CombatZone extends Attack implements AdventureCard, FlightDayPenalty, CrewmatePenalty {
    private final int crewmatePenalty = 2;
    private final int flightDayPenalty = 3;
    private HashMap<Player, Integer> playersAndEnginePower;
    private HashMap<Player, Double> playersAndFirePower;

    private Player playerWithLeastEnginePower;

    private FlightRules flightRules;

    public CombatZone(int crewmatePenalty, int flightDayPenalty, FlightRules flightRules) {
        super(createProjectiles());
        this.flightRules = flightRules;
        setPlayers();
    }
    private void setPlayers() {
        List<Player> players = flightRules.getPlayerOrder();
        playersAndEnginePower = new HashMap<>();
        playersAndFirePower = new HashMap<>();

        for (Player player : players) {
            ShipManager ship = player.getShipManager();
            playersAndEnginePower.put(player, ship.calculateEnginePower());
            playersAndFirePower.put(player, ship.calculateFirePower());
        }
    }
    private static HashMap<Projectile, Direction> createProjectiles() {
        Projectile smallProjectile = Projectile.SMALL;
        Projectile bigProjectile = Projectile.BIG;

        HashMap<Projectile, Direction> projectilesAndDirections = new HashMap<>();
        projectilesAndDirections.put(smallProjectile, Direction.UP);
        projectilesAndDirections.put(bigProjectile, Direction.UP);

        return projectilesAndDirections;
    }

    @Override
    public void play() {
        start(new StartState());
    }

    /**
     * Identifies the player with the fewest crewmates (humans + aliens).
     * <p>
     * If multiple players are tied for the lowest number of crewmates,
     * the tie is resolved by flight order: the player furthest ahead on the route
     * is selected.
     *
     * @return the player with the lowest number of crewmates, breaking ties by flight order
     */
    public Player findPlayerWithLeastCrewmates() {
        List<Player> players = flightRules.getPlayerOrder();

        int minCrewmates = getMinimumCrewmateCount(players);
        List<Player> playersWithLeastCrewmates = getPlayersWithCrewmatesNumber(players, minCrewmates);

        if (playersWithLeastCrewmates.size() == 1) {
            return playersWithLeastCrewmates.get(0);
        }
        return findLeadingPlayerAmong(players, playersWithLeastCrewmates);
    }
    private int getMinimumCrewmateCount(List<Player> players) {
        return players.stream()
            .mapToInt(p -> p.getShipManager().countCrewmates())
            .min()
            .orElseThrow(() -> new NotFoundException("No user found"));
    }
    private List<Player> getPlayersWithCrewmatesNumber(List<Player> players, int crewmatersNumber) {
        return players.stream()
            .filter(p -> p.getShipManager().countCrewmates() == crewmatersNumber)
            .collect(Collectors.toList());
    }
    private Player findLeadingPlayerAmong(List<Player> playersInFlightOrder, List<Player> players) {
        return playersInFlightOrder.stream()
            .filter(players::contains)
            .findFirst()
            .orElseThrow(() -> new NotFoundException("No user found"));
    }

    /**
     * Determines the player with the lowest engine power.
     * <p>
     * If multiple players share the same minimum engine power,
     * the tie is resolved by flight order: the player furthest ahead
     * is selected. The resulting player is stored in {@code playerWithLeastEnginePower}
     * for use in subsequent phases, such as applying the crewmate penalty.
     *
     * @return the player with the lowest engine power, with ties broken by flight order
     */
    public Player findPlayerWithLeastEnginePower() {
        int minEnginePower = getMinimumEnginePower();
        List<Player> playersWithLeastEnginePower = getPlayersWithEnginePower(minEnginePower);

        if (playersWithLeastEnginePower.size() == 1) {
            Player player = playersWithLeastEnginePower.get(0);
            playerWithLeastEnginePower = player;
            return player;
        }
        Player player = findLeadingPlayerAmong(flightRules.getPlayerOrder(), playersWithLeastEnginePower);
        playerWithLeastEnginePower = player;
        return player;
    }
    private int getMinimumEnginePower() {
        return playersAndEnginePower.keySet().stream()
            .mapToInt(p -> playersAndEnginePower.get(p))
            .min()
            .orElseThrow(() -> new NotFoundException("No user found"));
    }
    private List<Player> getPlayersWithEnginePower(int enginePower) {
        return playersAndEnginePower.keySet().stream()
            .filter(p -> playersAndEnginePower.get(p) == enginePower)
            .collect(Collectors.toList());
    }

    /**
     * Determines the player with the lowest firepower.
     * <p>
     * If multiple players share the same minimum firepower,
     * the tie is resolved by flight order: the player furthest ahead is selected.
     *
     * @return the player with the lowest firepower, with ties broken by flight order
     */
    public Player findPlayerWithLeastFirePower() {
        double minFirePower = getMinimumFirePower();
        List<Player> playersWithLeastFirePower = getPlayersWithFirePower(minFirePower);

        if (playersWithLeastFirePower.size() == 0) {
            Player player = playersWithLeastFirePower.get(0);
            setPlayer(player);
            return player;
        }
        Player player = findLeadingPlayerAmong(flightRules.getPlayerOrder(), playersWithLeastFirePower);
        setPlayer(player);
        return player;
    }
    private double getMinimumFirePower() {
        return playersAndFirePower.keySet().stream()
            .mapToDouble(p -> playersAndFirePower.get(p))
            .min()
            .orElseThrow(() -> new NotFoundException("No user found"));
    }
    private List<Player> getPlayersWithFirePower(double firePower) {
        return playersAndFirePower.keySet().stream()
            .filter(p -> playersAndFirePower.get(p) == firePower)
            .collect(Collectors.toList());
    }

    /**
     * Handles the player's selection of engines to activate for the engine power test.
     * <p>
     * The player may choose to activate double engines by providing a map of engine positions
     * and the battery slots used to power them.
     *
     * @param player the player making the engine selection
     * @param enginesAndBatteries a map where each key is the position of a double engine,
     *                            and each value is the list of battery slot positions used to activate it
     */
    public void selectEngines(Player player, HashMap<List<Integer>, List<Integer>> enginesAndBatteries) {
        ShipManager ship = player.getShipManager();
        int enginePower = (int) ship.activateComponent(enginesAndBatteries);

        int baseEnginePower = playersAndEnginePower.get(player);
        enginePower += baseEnginePower;

        playersAndEnginePower.put(player, enginePower);
        updateState();
    }

    /**
     * Called when the player chooses not to activate any engines during the engine power test.
     *
     * @param player the player who declines to activate engines
     */
    public void selectNoEngines(Player player) {
        updateState();
    }

    /**
     * Handles the player's selection of cannons to activate for the firepower test.
     * <p>
     * The player may choose to activate double cannons by providing a map of cannon positions
     * and the battery slots used to power them.
     *
     * @param player the player making the cannon selection
     * @param doubleCannonsAndBatteries a map where each key is the position of a double cannon,
     *                                  and each value is the list of battery slot positions used to activate it
     */
    public void selectCannons(Player player, HashMap<List<Integer>, List<Integer>> doubleCannonsAndBatteries) {
        ShipManager ship = player.getShipManager();
        double firePower = ship.activateComponent(doubleCannonsAndBatteries);

        double baseFirePower = playersAndFirePower.get(player);
        firePower += baseFirePower;

        playersAndFirePower.put(player, firePower);
        updateState();
    }

    /**
     * Called when the player chooses not to activate any cannons during the firepower test.
     * 
     * @param player the player who declines to activate cannons
     */
    public void selectNoCannons(Player player) {
        updateState();
    }
    
    /**
     * Executes the projectile attack on the player with the lowest firepower.
     * <p>
     * Two shots are fired from the rear of the ship: one small and one big.
     * <ul>
     *   <li><b>Small projectile:</b> Can be blocked if the player has an active shield facing the correct direction and a battery is spent. If blocked, the attack is skipped.</li>
     *   <li><b>Big projectile:</b> Cannot be blocked. If the shot hits the ship, the targeted component is destroyed.</li>
     * </ul>
     */
    @Override
    public void attack() {
        for (Projectile projectile : getProjectilesAndDirection().keySet()) {
            List<Integer> aimedCoords = aimAtCoordsWith(projectile);
            Direction direction = getProjectilesAndDirection().get(projectile);

            if (projectile == Projectile.SMALL) {
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

    @Override
    public int getCrewmatePenalty() {
        return crewmatePenalty;
    }

    @Override
    public void applyCrewmatePenalty(int shipRow, int shipColumn) {
        ShipManager ship = playerWithLeastEnginePower.getShipManager();

        ship.removeCrewmate(shipRow, shipColumn);
        updateState();
    }


    @Override
    public void applyFlightDayPenalty() {
        Player player = findPlayerWithLeastCrewmates();

        flightRules.movePlayerBackwards(flightDayPenalty, player);
    }

    public int getNumberOfBoardPlayers() {
        return flightRules.getPlayerOrder().size();
    }
}
