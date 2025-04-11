package it.polimi.it.galaxytrucker.model.managers;
import java.util.List;

import it.polimi.it.galaxytrucker.model.design.strategyPattern.FlightRules;

public class FlightBoardFlightRules implements FlightRules {
    private final FlightBoard flightBoard;

    public FlightBoardFlightRules(FlightBoard flightBoard) {
        this.flightBoard = flightBoard;
    }

    @Override
    public List<Player> getPlayerOrder() {
        return flightBoard.getPlayerOrder();
    }

    @Override
    public void movePlayerBackwards(int progress, Player player) {
        flightBoard.movePlayerBackwards(progress, player);
    }
}
