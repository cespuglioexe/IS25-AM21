package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.managers.Player;

import java.util.*;

public abstract class AdventureCard<T> {
    private Optional<List<Player>> partecipants;
    private Optional<Integer> penalty;
    private Optional<Integer> flightDayPenalty;
    private Optional<T> reward;

    public AdventureCard(Optional<List<Player>> partecipants, Optional<Integer> penalty, Optional<Integer> flightDayPenalty, Optional<T> reward) {
        this.partecipants = partecipants;
        this.penalty = penalty;
        this.flightDayPenalty = flightDayPenalty;
        this.reward = reward;
    }

    public AdventureCard() {

    }

    public Optional<List<Player>> getPartecipants() {
        return partecipants;
    }

    public Optional<Integer> getPenalty() {
        return penalty;
    }

    public Integer getFlightDayPenalty() {
        return flightDayPenalty;
    }

    public Optional<T> getReward() {
        return reward;
    }

    public abstract void play();
}
