package it.polimi.it.galaxytrucker.model.adventurecards.refactored;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.CardStateMachine;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.planets.StartState;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.planets.FlightDayPenaltyState;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCard;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.CargoReward;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.FlightDayPenalty;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.Participation;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.model.design.strategyPattern.FlightRules;
import it.polimi.it.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.model.managers.CargoManager;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.utility.Cargo;

/**
 * Represents the "Planets" adventure card in the game Galaxy Trucker.
 * <p>
 * This card allows multiple players to land on different planets to collect cargo rewards.
 * Players choose in flight order which planet to land on, and each planet may be occupied by only one player.
 * Once the planets are claimed, players take turns selecting cargo from their assigned planet.
 *
 * <ul>
 *   <li>Each player may choose to land on one of the available planets.</li>
 *   <li>Planets are claimed in flight order and cannot be chosen by more than one player.</li>
 *   <li>Each planet contains a list of {@link Cargo} items as rewards.</li>
 *   <li>Players take turns choosing cargo from their planet, loading or discarding items.</li>
 *   <li>Once all cargo is handled, each player who landed on a planet receives a flight day penalty.</li>
 * </ul>
 *
 * The card operates as a Finite State Machine (FSM) and progresses through the following states:
 * <pre>
 * StartState
 *     ↓
 * ParticipationState
 *     ↓
 * CargoRewardState
 *     ↓
 * FlightDayPenaltyState
 *     ↓
 * EndState
 * </pre>
 * Transitions are automatic and handled internally via {@code updateState()} and {@code changeState()}.
 *
 * <h2>Implementation Notes</h2>
 * <ul>
 *   <li>Implements {@link CargoReward} to allow manual cargo selection and discarding per planet.</li>
 *   <li>Implements {@link FlightDayPenalty} to apply movement penalties after rewards.</li>
 *   <li>Only players who landed on a planet participate in the cargo phase and receive penalties.</li>
 * </ul>
 * 
 * @author Stefano Carletto
 * @version 1.0
 *
 * @see AdventureCard
 * @see Participation
 * @see CargoReward
 * @see FlightDayPenalty
 * @see StateMachine
 */
public class Planets extends CardStateMachine implements AdventureCard, Participation<Cargo>, CargoReward, FlightDayPenalty {
    private Player currentPlayer;
    private HashMap<Integer, List<Cargo>> planetsAndRewards = new HashMap<>();
    private HashMap<Integer, Optional<Player>> planetsAndPlayers = new HashMap<>();
    private final int flightDayPenalty;

    private final FlightRules flightRules;

    public Planets(int numberOfPlanets, List<List<Cargo>> cargoRewardsByPlanet, int flightDayPenalty, FlightRules flightRules) {
        initializePlanets(numberOfPlanets);
        initializeRewards(cargoRewardsByPlanet);
        this.flightDayPenalty = flightDayPenalty;
        this.flightRules = flightRules;
    }
    private void initializePlanets(int numberOfPlanets) {
        for (int i = 0; i < numberOfPlanets; i++) {
            planetsAndRewards.put(i, List.of());
            planetsAndPlayers.put(i, Optional.empty());
        }
    }
    private void initializeRewards(List<List<Cargo>> rewards) {
        Iterator<Integer> planetsIterator = planetsAndRewards.keySet().iterator();
        int i = 0;
    
        while (planetsIterator.hasNext()) {
            int planet = planetsIterator.next();
            List<Cargo> rewardList = new ArrayList<>(rewards.get(i));
            planetsAndRewards.put(planet, rewardList);
            i++;
        }
    }

    @Override
    public void play() {
        start(new StartState());
    }

    /**
     * Allows the specified player to choose a planet, marking it as occupied.
     *
     * <p>This method checks whether the selected planet is already occupied. If so,
     * it throws an {@link InvalidActionException}. Otherwise, it marks the planet
     * as occupied by the given player.</p>
     *
     * @param player the player attempting to occupy a planet
     * @param choice the planet the player wants to occupy
     * @throws InvalidActionException if the selected planet is already occupied
     */
    @Override
    public void participate(Player player, int choice) throws InvalidActionException {
        if (!isChoiceValid(choice)) {
            throw new InvalidActionException("[" + choice + "] is not a valid choice");
        }
        if (isPlanetOccupied(choice)) {
            throw new InvalidActionException("The selected planet is occupied");
        }

        occupyPlanet(choice, player);
        updateState();
    }
    private boolean isChoiceValid(int choice) {
        if (choice < 0 || choice > planetsAndRewards.keySet().size() - 1 ) {
            return false;
        }
        return true;
    }
    private boolean isPlanetOccupied(int planet) {
        return planetsAndPlayers.get(planet).isPresent();
    }
    private void occupyPlanet(int planet, Player player) throws InvalidActionException {
        planetsAndPlayers.put(planet, Optional.of(player));
    }

    @Override
    public void decline(Player player) {
        updateState();
    }

    public void initializeFirstPlayer() {
        currentPlayer = getActivePlayersInPlanetOrder().get(0);
    }

    /**
     * Returns the list of available cargo rewards for each planet.
     *
     * <p>Each element in the returned list corresponds to a planet and contains
     * the set of {@link Cargo} items that can be obtained from that planet.</p>
     *
     * @return a list where each set represents the cargo rewards of a planet
     */
    @Override
    public List<List<Cargo>> getChoices() {
        List<List<Cargo>> choices = new ArrayList<>();

        List<Integer> planets = getPlanets();
        for (Integer planet : planets) {
            List<Cargo> reward = getPlanetReward(planet);

            choices.add(reward);
        }

        return choices;
    }
    public List<Integer> getPlanets() {
        return new ArrayList<>(planetsAndRewards.keySet());
    }
    private List<Cargo> getPlanetReward(int planet) {
        return planetsAndRewards.get(planet);
    }

    @Override
    public HashMap<Integer, Player> getTakenChoices() {
        HashMap<Integer, Player> occupiedPlanets = new HashMap<>();

        for (Integer planet : planetsAndPlayers.keySet()) {
            if (isPlanetOccupied(planet)) {
                occupiedPlanets.put(planet, planetsAndPlayers.get(planet).get());
            }
        }
        return occupiedPlanets;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Returns the index of the planet currently occupied by the specified player.
     * <p>
     *
     * @param targetPlayer the player whose occupied planet is to be found
     * @return the index of the planet occupied by the player
     * @throws IllegalArgumentException if the player is not occupying any planet
     */
    public int getOccupiedPlanetFromPlayer(Player targetPlayer) {
        return planetsAndPlayers.entrySet().stream()
            .filter(entry -> isOccupiedByPlayer(entry, targetPlayer))
            .map(Map.Entry::getKey)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Player is not occupying a planet."));
    }
    private boolean isOccupiedByPlayer(Map.Entry<Integer, Optional<Player>> entry, Player targetPlayer) {
        return entry.getValue().isPresent() && entry.getValue().get().equals(targetPlayer);
    }

    @Override
    public List<Cargo> getCargoReward() {
        int planet = getOccupiedPlanetFromPlayer(currentPlayer);
        return planetsAndRewards.get(planet);
    }

    @Override
    public void acceptCargo(int loadIndex,int row, int column) {
        int planet = getOccupiedPlanetFromPlayer(currentPlayer);
        Cargo cargo = removeCargoFromPlanet(planet, loadIndex);
        CargoManager.manageCargoAddition(cargo, List.of(row, column), currentPlayer);

        updateState();

        List<Cargo> cargoList = planetsAndRewards.get(planet);
        if (cargoList.isEmpty()) {
            nextPlayer();
        }
    }
    private void nextPlayer() {
        List<Player> activePlayers = getActivePlayersInPlanetOrder();
    
        int currentIndex = findCurrentPlayerIndex(activePlayers);
        boolean hasNextPlayer = currentIndex + 1 < activePlayers.size();
    
        if (hasNextPlayer) {
            currentPlayer = activePlayers.get(currentIndex + 1);
            updateState();
        } else {
            changeState(new FlightDayPenaltyState());
        }
    }
    private List<Player> getActivePlayersInPlanetOrder() {
        return planetsAndPlayers.values().stream()
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
    }
    private int findCurrentPlayerIndex(List<Player> players) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).equals(currentPlayer)) {
                return i;
            }
        }
        throw new IllegalStateException("Current player not found in active players list.");
    }
    private Cargo removeCargoFromPlanet(int planet, int loadIndex) {
        List<Cargo> cargoList = planetsAndRewards.get(planet);
        Cargo cargo = cargoList.get(loadIndex);
        cargoList.remove(loadIndex);
        
        return cargo;
    }

    @Override
    public void discardCargo(int loadIndex) {
        int planet = getOccupiedPlanetFromPlayer(currentPlayer);
        removeCargoFromPlanet(planet, loadIndex);

        updateState();

        List<Cargo> cargoList = planetsAndRewards.get(planet);
        if (cargoList.isEmpty()) {
            nextPlayer();
        }
    }

    @Override
    public void applyFlightDayPenalty() {
        List<Player> players = getPlayerReverseOrder();

        for (Player player : players) {
            flightRules.movePlayerBackwards(flightDayPenalty, player);
        }
    }

    private List<Player> getPlayerReverseOrder() {
        return getActualPlayersFrom(flightRules.getPlayerOrder().reversed());
    }

    private List<Player> getActualPlayersFrom(List<Player> players) {
        return planetsAndPlayers.keySet().stream()
                .map(planetsAndPlayers::get)
                .flatMap(Optional::stream)
                .filter(players::contains)
                .collect(Collectors.toList());
    }

    public int getNumberOfBoardPlayers() {
        return flightRules.getPlayerOrder().size();
    }
}
