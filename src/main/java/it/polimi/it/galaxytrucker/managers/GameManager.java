package it.polimi.it.galaxytrucker.managers;

import it.polimi.it.galaxytrucker.componenttiles.CargoHold;
import it.polimi.it.galaxytrucker.componenttiles.ComponentTile;
import it.polimi.it.galaxytrucker.componenttiles.SpecialCargoHold;
import it.polimi.it.galaxytrucker.exceptions.IllegalComponentPositionException;
import it.polimi.it.galaxytrucker.gameStates.EndGame;
import it.polimi.it.galaxytrucker.gameStates.Fixing;
import it.polimi.it.galaxytrucker.utility.Cargo;

import java.util.*;

public class GameManager implements EndGame, Fixing {
    int level;
    List<Player> players;
    FlightBoardState flightBoardState;

    public GameManager(int level) {
        this.level = level;
        players = new ArrayList<>();
        flightBoardState = new FlightBoardState(level == 1 ? 18 : 24);
    }

    public int getLevel(){
        return level;
    }

    public FlightBoardState getFlightBoardState() {
        return flightBoardState;
    }

    //<editor-fold desc="Interface - Fixing">

    @Override
    public void playerRemovesComponent(int playerID, int xCoordinate, int yCoordinate) {
        ShipManager shipManager = players.get(playerID).getShipManager();

        try {
            shipManager.removeComponentTile(xCoordinate, yCoordinate);
        }
        catch (Exception e) {
            if (e instanceof IndexOutOfBoundsException) {
                // do something
            }
            else if (e instanceof IllegalComponentPositionException) {
                // do something
            }
        }
    }

    @Override
    public void playerFinishedFixing(int playerID) {

    }

    //</editor-fold>


    //<editor-fold desc="Interface - EndGame">

    @Override
    public Map<Integer, Integer> calculateFinalScores() {
        int bestShipPlayer = 0;
        int bestShipConnectors = Integer.MAX_VALUE;
        Map<Integer, Integer> scores = new HashMap<>();

        for (Player player : players) {
            int credits = countPlayerCredits(player.getPlayerID());
            int exposed = player.getShipManager().countAllExposedConnectors();

            if (bestShipConnectors > exposed) {
                bestShipConnectors = exposed;
                bestShipPlayer = player.getPlayerID();
            }

            scores.put(player.getPlayerID(), credits);
        }

        scores.replace(bestShipPlayer, scores.get(bestShipPlayer) + 2);

        return scores;
    }

    private int countPlayerCredits(int PlayerID) {
        int totCredits = 0;
        Player player = players.get(PlayerID);
        ShipManager shipManager = player.getShipManager();

        totCredits += player.getCredits();

        // Evaluate cargo value in regular cargo holds
        Set<List<Integer>> cargoHolds = shipManager.getAllComponentsPositionOfType(CargoHold.class);
        for (List<Integer> cargoHold : cargoHolds) {
            Optional<ComponentTile> component = shipManager.getComponent(cargoHold.get(0), cargoHold.get(1));
            if (component.isPresent()) {
                List<Cargo> cargos = ((CargoHold)component.get()).getContainedCargo();
                for (Cargo cargo : cargos) {
                    totCredits += cargo.getColor().ordinal() + 1;
                }
            }
        }

        // Evaluate cargo value in special cargo holds
        Set<List<Integer>> specialCargoHolds = shipManager.getAllComponentsPositionOfType(SpecialCargoHold.class);
        for (List<Integer> specialCargoHold : specialCargoHolds) {
            Optional<ComponentTile> component = shipManager.getComponent(specialCargoHold.get(0), specialCargoHold.get(1));
            if (component.isPresent()) {
                List<Cargo> cargos = ((CargoHold)component.get()).getContainedCargo();
                for (Cargo cargo : cargos) {
                    totCredits += cargo.getColor().ordinal() + 1;
                }
            }
        }

        // Subtract credits for lost components
        // totCredits -= shipManager.getDiscardedComponents();

        /*
         *  Add credits for finishing order.
         *
         *  - LVL I: 4, 3, 2, 1
         *  - LVL II: 8, 6, 4, 2
         */
        totCredits += (4 - flightBoardState.getPlayerOrder().indexOf(PlayerID)) * level;

        return totCredits;
    }
    //</editor-fold>


}
