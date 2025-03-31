package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.cardStates.StartCardState;
import it.polimi.it.galaxytrucker.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.managers.Player;

import java.util.*;

public abstract class AdventureCard<T> extends StateMachine {
    private List<Player> partecipants;
    private Optional<Integer> penalty; //Penalità generidca che dopo viene implementata da cargopenalty e crewmatepenalty
    private Optional<Integer> flightDayPenalty;
    private Optional<T> reward;
    private int firePowerRequired;
    private int creditReward;

    // Gli stati che non richiedono partecipanti, controllano che i player siano tutti attivi.
    // Altrimenti se lo stato richiede partecipazione, quando chiami addPartecipants() in update controlli i parametri
    // della carta o se tutti i giocatori hanno preso una decisione(UPDATE)

    // Si applica l'effetto della carta

    public AdventureCard(Optional<Integer> penalty, Optional<Integer> flightDayPenalty, Optional<T> reward, int firePowerRequired, int creditReward) {
        start(new StartCardState());
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
