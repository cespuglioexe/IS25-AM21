package it.polimi.it.galaxytrucker.model.adventurecards;

import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.model.managers.Player;

import java.util.*;

public abstract class AdventureCard<T> extends StateMachine {
    private List<Player> participants;
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
        this.participants = new ArrayList<>();
    }

    public List<Player> getParticipants() {
        return participants;
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

    public void setParticipants(List<Player> participants) {
        this.participants = participants;
    }

    public void addPlayer(Player player){
        participants.add(player);
    }

    public void setPlayersInOrder(List<UUID> playersInOrder) {
        this.playersInOrder = playersInOrder;
    }
}
