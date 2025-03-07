package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.managers.Player;

import java.util.*;

public abstract class AdventureCard<T> {
    private Optional<List<Player>> partecipants;
    private Optional<Integer> penalty; //Penalità generidca che dopo viene implementata da cargopenalty e crewmatepenalty
    private Optional<Integer> flightDayPenalty;
    private Optional<T> reward;
    private Optional<Integer> firePowerRequired;

    public AdventureCard(Optional<List<Player>> partecipants, Optional<Integer> penalty, Optional<Integer> flightDayPenalty, Optional<T> reward) {
        this.partecipants = partecipants;
        this.penalty = penalty;
        this.flightDayPenalty = flightDayPenalty;
        this.reward = reward;
    }

    public Optional<List<Player>> getPartecipants() {
        return partecipants;
    }

    public Optional<Integer> getPenalty() {
        return penalty;
    }

    public Optional<Integer> getFlightDayPenalty() {
        return flightDayPenalty;
    }

    public Optional<T> getReward() {
        return reward;
    }

    public abstract void play();
}
