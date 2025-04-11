package it.polimi.it.galaxytrucker.model.aventurecard.cards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import it.polimi.it.galaxytrucker.model.aventurecard.interfaces.AdventureCard;
import it.polimi.it.galaxytrucker.model.aventurecard.interfaces.CargoReward;
import it.polimi.it.galaxytrucker.model.aventurecard.interfaces.FlightDayPenalty;
import it.polimi.it.galaxytrucker.model.aventurecard.interfaces.Participation;
import it.polimi.it.galaxytrucker.model.design.strategyPattern.FlightRules;
import it.polimi.it.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.utility.Cargo;

public class Planets implements AdventureCard, Participation<Cargo>, CargoReward, FlightDayPenalty {
    private HashMap<Integer, Set<Cargo>> planetsAndRewards = new HashMap<>();
    private HashMap<Integer, Optional<Player>> planetsAndPlayers = new HashMap<>();
    private final int flightDayPenalty;

    private final FlightRules flightRules;

    public Planets(int numberOfPlanets, List<Set<Cargo>> cargoRewardsByPlanet, int flightDayPenalty, FlightRules flightRules) {
        initializePlanets(numberOfPlanets);
        initializeRewards(cargoRewardsByPlanet);
        this.flightDayPenalty = flightDayPenalty;
        this.flightRules = flightRules;
    }
    private void initializePlanets(int numberOfPlanets) {
        for (int i = 0; i < numberOfPlanets; i++) {
            planetsAndRewards.put(i, Set.of());
            planetsAndPlayers.put(i, Optional.empty());
        }
    }
    private void initializeRewards(List<Set<Cargo>> rewards) {
        Iterator<Integer> planetsIterator = planetsAndRewards.keySet().iterator();
        int i = 0;

        while (planetsIterator.hasNext()) {
            int planet = planetsIterator.next();

            planetsAndRewards.put(planet, rewards.get(i));
            i++;
        }
    }

    @Override
    public void play() {

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
        if (isPlanetOccupied(choice)) {
            throw new InvalidActionException("The selected planet is occupied");
        }

        occupyPlanet(choice, player);
    }
    private boolean isPlanetOccupied(int planet) {
        return planetsAndPlayers.get(planet).isPresent();
    }
    private void occupyPlanet(int planet, Player player) {
        planetsAndPlayers.put(planet, Optional.of(player));
    }

    @Override
    public void decline(Player player) {

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
    public List<Set<Cargo>> getChoices() {
        List<Set<Cargo>> choices = new ArrayList<>();

        List<Integer> planets = getPlanets();
        for (Integer planet : planets) {
            Set<Cargo> reward = getPlanetReward(planet);

            choices.add(reward);
        }

        return choices;
    }
    private List<Integer> getPlanets() {
        return new ArrayList<>(planetsAndRewards.keySet());
    }
    private Set<Cargo> getPlanetReward(int planet) {
        return planetsAndRewards.get(planet);
    }

    /**
     * Returns the total cargo reward collected from all selected planets.
     *
     * <p>This method aggregates all {@link Cargo} items available as rewards
     * across the selected planets and returns them as a single set.</p>
     *
     * @return a set containing all cargo rewards from all the planets
     */
    @Override
    public Set<Cargo> getCargoReward() {
        Set<Cargo> cargoReward = new HashSet<>();

        List<Integer> planets = getPlanets();
        for (Integer planet : planets) {
            Set<Cargo> planetReward = getPlanetReward(planet);

            cargoReward.addAll(planetReward);
        }

        return cargoReward;
    }

    /**
     * Applies the flight day penalty to all players who have landed on a planet.
     *
     * <p>The penalty is applied in reverse player order. Each affected player
     * is moved backwards on the flight board by a fixed number of steps
     * defined by the flight day penalty value.</p>
     */
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
}
