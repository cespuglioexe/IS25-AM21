package it.polimi.it.galaxytrucker.model.adventurecards.cards;

import java.util.HashMap;
import java.util.List;

import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.CardStateMachine;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.openspace.StartState;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCard;
import it.polimi.it.galaxytrucker.model.design.strategyPattern.FlightRules;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;

public class OpenSpace extends CardStateMachine implements AdventureCard {
    private HashMap<Player, Integer> playersAndEnginePower;

    private FlightRules flightRules;

    public OpenSpace(FlightRules flightRules) {
        this.flightRules = flightRules;
        setPlayers();
    }
    private void setPlayers() {
        List<Player> players = flightRules.getPlayerOrder();
        playersAndEnginePower = new HashMap<>();

        for (Player player : players) {
            ShipManager ship = player.getShipManager();
            playersAndEnginePower.put(player, ship.calculateEnginePower());
        }
    }

    @Override
    public void play() { start(new StartState());}

    public void selectEngine(Player player, HashMap<List<Integer>, List<Integer>> doubleEngineAndBatteries) {
        ShipManager ship = player.getShipManager();

        int enginePower = (int) ship.activateComponent(doubleEngineAndBatteries);

        int baseEnginePower = playersAndEnginePower.get(player);
        enginePower += baseEnginePower;

        playersAndEnginePower.put(player, enginePower);
        updateState();
    }

    public void travel() {
        List<Player> players = flightRules.getPlayerOrder();

        for (Player player : players) {
            int enginePower = playersAndEnginePower.get(player);

            flightRules.movePlayerForward(enginePower, player);
        }
    }

    public int getNumberOfPlayer(){
        return playersAndEnginePower.keySet().size();
    }

}
