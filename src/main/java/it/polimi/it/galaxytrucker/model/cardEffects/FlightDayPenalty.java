package it.polimi.it.galaxytrucker.model.cardEffects;

import it.polimi.it.galaxytrucker.model.managers.FlightBoardState;
import it.polimi.it.galaxytrucker.model.managers.Player;

public interface FlightDayPenalty {
    void applyFlightDayPenalty(FlightBoardState board, Player player);
}
