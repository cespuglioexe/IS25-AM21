package it.polimi.it.galaxytrucker.cardEffects;

import it.polimi.it.galaxytrucker.managers.Player;

public interface FlightDayPenalty extends Penalty<Integer> {
    @Override
    void applyPenalty(Integer penalty, Player player);
}
