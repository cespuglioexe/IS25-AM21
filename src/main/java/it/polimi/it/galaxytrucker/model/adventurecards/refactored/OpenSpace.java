package it.polimi.it.galaxytrucker.model.adventurecards.refactored;

import java.util.HashMap;
import java.util.List;

import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCard;
import it.polimi.it.galaxytrucker.model.design.strategyPattern.FlightRules;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;

public class OpenSpace implements AdventureCard {
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
    public void play() {

    }

    public void selectEngine(Player player, List<Integer> doubleEngine, List<Integer> battery) {
        ShipManager ship = player.getShipManager();
        HashMap<List<Integer>, List<Integer>> engineAndBattery = new HashMap<>();

        engineAndBattery.put(doubleEngine, battery);
        int enginePower = (int) ship.activateComponent(engineAndBattery);

        int baseEnginePower = playersAndEnginePower.get(player);
        enginePower += baseEnginePower;

        playersAndEnginePower.put(player, enginePower);
    }

    public void finishedSelectingEngines(Player player) {

    }

    public void travel() {
        List<Player> players = flightRules.getPlayerOrder();

        for (Player player : players) {
            int enginePower = playersAndEnginePower.get(player);

            flightRules.movePlayerForward(enginePower, player);
        }
    }
}
