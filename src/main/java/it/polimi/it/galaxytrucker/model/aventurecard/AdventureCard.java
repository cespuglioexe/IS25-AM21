package it.polimi.it.galaxytrucker.model.aventurecard;

import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.model.managers.Player;

import java.util.*;

public abstract class AdventureCard<T> extends StateMachine {
    private List<Player> partecipants;
    private Optional<Integer> penalty; //Penalità generidca che dopo viene implementata da cargopenalty e crewmatepenalty
    private Optional<Integer> flightDayPenalty;
    private Optional<T> reward;
    private int firePowerRequired;
    private int creditReward;
    private List<UUID> playersInOrder;

    public AdventureCard(Optional<Integer> penalty, Optional<Integer> flightDayPenalty, Optional<T> reward, int firePowerRequired, int creditReward) {
        this.penalty = penalty;
        this.flightDayPenalty = flightDayPenalty;
        this.reward = reward;
        this.firePowerRequired = firePowerRequired;
        this.creditReward = creditReward;
        this.partecipants = new ArrayList<>();
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

    public List<UUID> getPlayersInOrder() {
        return this.playersInOrder;
    }

    public void setPartecipants(List<Player> partecipants) {
        this.partecipants = partecipants;
    }

    public void addPlayer(Player player){
        partecipants.add(player);
    }

    public void setPlayersInOrder(List<UUID> playersInOrder) {
        this.playersInOrder = playersInOrder;
    }
}
