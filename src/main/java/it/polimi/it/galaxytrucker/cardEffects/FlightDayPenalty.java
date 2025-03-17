package it.polimi.it.galaxytrucker.cardEffects;

import it.polimi.it.galaxytrucker.managers.FlightBoardState;
import it.polimi.it.galaxytrucker.managers.Player;

public interface FlightDayPenalty {
    void applyFlightDayPenalty(FlightBoardState board, Player player);
}
