package it.polimi.it.galaxytrucker.model.aventurecard.refactored;

import java.util.List;

import it.polimi.it.galaxytrucker.model.aventurecard.interfaces.AdventureCard;
import it.polimi.it.galaxytrucker.model.aventurecard.interfaces.FlightDayPenalty;
import it.polimi.it.galaxytrucker.model.design.strategyPattern.FlightRules;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;

public class StarDust implements AdventureCard, FlightDayPenalty {
    private Player currentPlayer;
    private int flightDayPenalty;
    private final FlightRules flightRules;

    public StarDust(FlightRules flightRules) {
        this.flightRules = flightRules;
    }
    
    @Override
    public void play() {
        List<Player> players = flightRules.getPlayerOrder();

        for (Player player : players) {
            currentPlayer = player;
            flightDayPenalty = countExposedConnectorsOf(currentPlayer);

            applyFlightDayPenalty();
        }
    }
    private int countExposedConnectorsOf(Player player) {
        ShipManager ship = player.getShipManager();

        return ship.countAllExposedConnectors();
    }

    @Override
    public void applyFlightDayPenalty() {
        flightRules.movePlayerBackwards(flightDayPenalty, currentPlayer);
    }

}
