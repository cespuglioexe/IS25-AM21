package it.polimi.it.galaxytrucker.cardEffects;

public interface FlightDayPenalty extends Penalty<Integer> {
    @Override
    void applyPenalty(Integer penalty);
}
