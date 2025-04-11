package it.polimi.it.galaxytrucker.model.cardEffects;

import it.polimi.it.galaxytrucker.model.managers.FlightBoard;
import it.polimi.it.galaxytrucker.model.managers.Player;

public interface FlightDayPenalty {
    void applyFlightDayPenalty(FlightBoard board, Player player);
}
