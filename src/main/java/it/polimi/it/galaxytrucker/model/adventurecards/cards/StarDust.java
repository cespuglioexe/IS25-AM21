package it.polimi.it.galaxytrucker.model.adventurecards.cards;

import java.util.List;

import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.CardStateMachine;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.stardust.StartState;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCard;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCardInputContext;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCardVisitor;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.FlightDayPenalty;
import it.polimi.it.galaxytrucker.model.design.strategyPattern.FlightRules;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;

public class StarDust extends CardStateMachine implements AdventureCard, FlightDayPenalty {
    private Player currentPlayer;
    private int flightDayPenalty;
    private  FlightRules flightRules;
    private String graphic;

    public StarDust(FlightRules flightRules) {
        this.flightRules = flightRules;
    }

    public StarDust(int flightDayPenalty, FlightRules flightRules, String graphic) {
        this.flightRules = flightRules;
        this.graphic = graphic;
        this.flightDayPenalty = flightDayPenalty;
    }
    
    @Override
    public void play() { start(new StartState());}

    public void applyEffect(){
        List<Player> players = flightRules.getPlayerOrder();

        for (Player player : players) {
            currentPlayer = player;
            flightDayPenalty = countExposedConnectorsOf(currentPlayer);

            applyFlightDayPenalty();
        }
    }

    @Override
    public String getGraphicPath() {
        return graphic;
    }

    private int countExposedConnectorsOf(Player player) {
        ShipManager ship = player.getShipManager();

        return ship.countAllExposedConnectors();
    }

    @Override
    public void applyFlightDayPenalty() {
        flightRules.movePlayerBackwards(flightDayPenalty, currentPlayer);
    }

    @Override
    public String toString() {
        return "StarDust{" +
                "flightDayPenalty=" + flightDayPenalty +
                '}';
    }

    @Override
    public void accept(AdventureCardVisitor visitor, AdventureCardInputContext context) {
        
    }
}
