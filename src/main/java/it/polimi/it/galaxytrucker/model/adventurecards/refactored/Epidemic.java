package it.polimi.it.galaxytrucker.model.adventurecards.refactored;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.epidemic.EndState;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.epidemic.StartState;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCard;
import it.polimi.it.galaxytrucker.model.componenttiles.CabinModule;
import it.polimi.it.galaxytrucker.model.componenttiles.CentralCabin;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.model.design.strategyPattern.FlightRules;
import it.polimi.it.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;

/**
 * Represents the "Epidemic" adventure card in the game Galaxy Trucker.
 * <p>
 * This card simulates the spread of an epidemic on each player's ship.
 * If at least one cabin (either a CabinModule or CentralCabin) in a connected group
 * contains crewmates, the epidemic spreads and removes crewmates from all cabins
 * in that group.
 * <p>
 * The behavior is implemented using a finite state machine pattern. The card
 * transitions through a {@link StartState} and ends in an {@link EndState}
 * after applying the epidemic effect.
 * 
 * @author Stefano Carletto
 * @version 1.0
 *
 * @see AdventureCard
 * @see StateMachine
 */
public class Epidemic extends StateMachine implements AdventureCard {
    private FlightRules flightRules;

    public Epidemic(FlightRules flightRules) {
        this.flightRules = flightRules;
    }

    @Override
    public void play() {
        start(new StartState());
        
        spreadEpidemy();

        changeState(new EndState());     
    }

    private void spreadEpidemy() {
        for (Player player : flightRules.getPlayerOrder()) {
            ShipManager ship = player.getShipManager();

            removeCrewmatesNearCabins(ship);
        }
    }
    private void removeCrewmatesNearCabins(ShipManager ship) {
        Set<List<Integer>> cabinsCoords = new HashSet<>();
        cabinsCoords.addAll(ship.getAllComponentsPositionOfType(CabinModule.class));
        cabinsCoords.addAll(ship.getAllComponentsPositionOfType(CentralCabin.class));

        List<Set<List<Integer>>> cabinBranches = getCabinBranches(cabinsCoords, ship);

        for (Set<List<Integer>> branch : cabinBranches) {
            removeCrewmatesFromBranchIfAnyInfected(branch, ship);
        }
    }
    private List<Set<List<Integer>>> getCabinBranches(Set<List<Integer>> cabins, ShipManager ship) {
        List<Set<List<Integer>>> cabinBranches = new ArrayList<>();
        Set<List<Integer>> visitedCabins = new HashSet<>();

        for (List<Integer> cabin : cabins) {
            if (!visitedCabins.contains(cabin)) {
                Set<List<Integer>> newBranch = new HashSet<>();

                getBranchOfCabin(cabin, ship, newBranch);
                visitedCabins.addAll(newBranch);
                cabinBranches.add(newBranch);
            }
        }
        return cabinBranches;
    }
    private void removeCrewmatesFromBranchIfAnyInfected(Set<List<Integer>> branch, ShipManager ship) {
        if (branch.size() == 1) {
            return;
        }
        for (List<Integer> cabin : branch) {
            removeCrewmateAt(ship, cabin);
        }
    }
    private void getBranchOfCabin(List<Integer> cabinCoords, ShipManager ship, Set<List<Integer>> branch) {
        CentralCabin cabin = (CentralCabin) ship.getComponent(cabinCoords.get(0), cabinCoords.get(1)).get();

        if (cabin.getCrewmates().isEmpty()) {
            return;
        }
        if (branch.contains(cabinCoords)) {
            return;
        }

        branch.add(cabinCoords);

        for (List<Integer> adjacentCabin : getAdjacentCabins(ship, cabinCoords)) {
            getBranchOfCabin(adjacentCabin, ship, branch);
        }
    }
    private void removeCrewmateAt(ShipManager ship, List<Integer> pos) {
        try {
            ship.removeCrewmate(pos.get(0), pos.get(1));
        } catch (InvalidActionException e) {
            
        }
    }
    private List<List<Integer>> getAdjacentCabins(ShipManager ship, List<Integer> referenceCabin) {
        return Stream.concat(
                ship.getAllComponentsPositionOfType(CabinModule.class).stream(),
                ship.getAllComponentsPositionOfType(CentralCabin.class).stream()
            )
            .filter(cabin -> isAdjacent(cabin, referenceCabin))
            .toList();
    }
    private boolean isAdjacent(List<Integer> a, List<Integer> b) {
        int rowDiff = Math.abs(a.get(0) - b.get(0));
        int colDiff = Math.abs(a.get(1) - b.get(1));
        return (rowDiff == 1 && colDiff == 0) || (rowDiff == 0 && colDiff == 1);
    }
}
