package it.polimi.it.galaxytrucker.model.adventurecards.refactored;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCard;
import it.polimi.it.galaxytrucker.model.componenttiles.CabinModule;
import it.polimi.it.galaxytrucker.model.componenttiles.CentralCabin;
import it.polimi.it.galaxytrucker.model.design.strategyPattern.FlightRules;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;

public class Epidemic implements AdventureCard {
    private FlightRules flightRules;

    public Epidemic(FlightRules flightRules) {
        this.flightRules = flightRules;
    }

    @Override
    public void play() {
        List<Player> players = flightRules.getPlayerOrder();

        for (Player currentPlayer : players) {
            int delCentre;
            int del;
            ShipManager ship = currentPlayer.getShipManager();
            Set<List<Integer>> centralCabinCoords = ship.getAllComponentsPositionOfType(CentralCabin.class);

            List<Integer> positionCentre = centralCabinCoords.stream()
                    .flatMap(List::stream)
                    .collect(Collectors.toList());

            delCentre=0;
            for (List<Integer> position :  currentPlayer.getShipManager().getAllComponentsPositionOfType(CabinModule.class)) {
                del = 0;
                if(!positionCentre.isEmpty()) {
                    if (position.get(0) == positionCentre.get(0) + 1 || position.get(0) == positionCentre.get(0) - 1) {
                        if(del == 0 && position.get(1) == positionCentre.get(1)) {
                            currentPlayer.getShipManager().removeCrewmate(position.get(0), position.get(1));
                            del = 1;
                            if(delCentre==0){
                                delCentre++;
                                currentPlayer.getShipManager().removeCrewmate(positionCentre.get(0), positionCentre.get(1));
                            }
                        }
                    } else{
                        if (position.get(1) == positionCentre.get(1) + 1 || position.get(1) == positionCentre.get(1) - 1) {
                            if(del == 0 && position.get(0) == positionCentre.get(0)) {
                                currentPlayer.getShipManager().removeCrewmate(position.get(0), position.get(1));
                                del = 1;
                            }
                        }
                    }
                }
                for (List<Integer> support : currentPlayer.getShipManager().getAllComponentsPositionOfType(CabinModule.class)) {
                    if (position.get(0) == support.get(0) + 1 || position.get(0) == support.get(0) - 1) {
                        if(del == 0 && position.get(1) == support.get(1)) {
                            currentPlayer.getShipManager().removeCrewmate(position.get(0), position.get(1));
                            del = 1;
                        }
                    } else{
                        if (position.get(1) == support.get(1) + 1 || position.get(1) == support.get(1) - 1) {
                            if(del == 0 && position.get(0) == support.get(0)) {
                                currentPlayer.getShipManager().removeCrewmate(position.get(0), position.get(1));
                                del = 1;
                            }
                        }
                    }
                }
            }
        }        
    }
}
