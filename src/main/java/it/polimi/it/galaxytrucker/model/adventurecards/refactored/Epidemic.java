package it.polimi.it.galaxytrucker.model.adventurecards.refactored;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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

public class Epidemic extends StateMachine implements AdventureCard {
    private FlightRules flightRules;

    public Epidemic(FlightRules flightRules) {
        this.flightRules = flightRules;
    }

    @Override
    public void play() {
        start(new StartState());
        
        removeCrewmatesNearCabins();

        changeState(new EndState());     
    }

    private void removeCrewmatesNearCabins() {
        for (Player player : flightRules.getPlayerOrder()) {
            ShipManager ship = player.getShipManager();

            Optional<List<Integer>> centralCabin = getCentralCabinCoords(ship);
            if (centralCabin.isPresent()) {
                removeCrewmatesAdjacentToCentralCabin(ship, centralCabin.get());
            }
            removeCrewmatesAdjacentToOtherCabins(ship);
        }
    }
    private Optional<List<Integer>> getCentralCabinCoords(ShipManager ship) {
        return ship.getAllComponentsPositionOfType(CentralCabin.class).stream().findFirst();
    }
    private void removeCrewmatesAdjacentToCentralCabin(ShipManager ship, List<Integer> centralCabinPos) {
        List<List<Integer>> adjacentCabins = getAdjacentCabins(ship, centralCabinPos);
    
        for (List<Integer> pos : adjacentCabins) {
            removeCrewmateAt(ship, pos);
        }
    
        removeCrewmateAt(ship, centralCabinPos);
    }
    private void removeCrewmatesAdjacentToOtherCabins(ShipManager ship) {
        Set<List<Integer>> allCabinPositions = ship.getAllComponentsPositionOfType(CabinModule.class);
    
        for (List<Integer> cabin : allCabinPositions) {
            if (isAdjacentToAnyCabin(cabin, allCabinPositions)) {
                removeCrewmateAt(ship, cabin);
            }
        }
    }
    private List<List<Integer>> getAdjacentCabins(ShipManager ship, List<Integer> referenceCabin) {
        return ship.getAllComponentsPositionOfType(CabinModule.class).stream()
            .filter(cabin -> isAdjacent(cabin, referenceCabin))
            .toList();
    }
    private boolean isAdjacentToAnyCabin(List<Integer> target, Set<List<Integer>> cabins) {
        return cabins.stream()
            .anyMatch(other -> !other.equals(target) && isAdjacent(target, other));
    }
    private boolean isAdjacent(List<Integer> a, List<Integer> b) {
        int rowDiff = Math.abs(a.get(0) - b.get(0));
        int colDiff = Math.abs(a.get(1) - b.get(1));
        return (rowDiff == 1 && colDiff == 0) || (rowDiff == 0 && colDiff == 1);
    }
    private void removeCrewmateAt(ShipManager ship, List<Integer> pos) {
        try {
            ship.removeCrewmate(pos.get(0), pos.get(1));
        } catch (InvalidActionException e) {
            
        }
    }
}
