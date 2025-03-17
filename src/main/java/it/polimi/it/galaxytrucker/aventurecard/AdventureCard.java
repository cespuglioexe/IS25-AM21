package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.managers.Player;

import java.util.*;

public abstract class AdventureCard<T> {
    private List<Player> partecipants;
    private Optional<Integer> penalty; //Penalità generidca che dopo viene implementata da cargopenalty e crewmatepenalty
    private Optional<Integer> flightDayPenalty;
    private Optional<T> reward;
    private int firePowerRequired;
    private int creditReward;

    public AdventureCard(Optional<Integer> penalty, Optional<Integer> flightDayPenalty, Optional<T> reward, int firePowerRequired, int creditReward) {
        this.penalty = penalty;
        this.flightDayPenalty = flightDayPenalty;
        this.reward = reward;
        this.firePowerRequired = firePowerRequired;
        this.creditReward = creditReward;
    }

    public List<Player> getPartecipants() {
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

    public int getFirePowerRequired() {
        return firePowerRequired;
    }
    public int getCreditReward() {
        return creditReward;
    }

    public void setPartecipants(List<Player> partecipants) {
        this.partecipants = partecipants;
    }

}
