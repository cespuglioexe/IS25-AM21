package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.openspace;

import java.util.List;

import it.polimi.it.galaxytrucker.model.adventurecards.cardevents.InputNeeded;
import it.polimi.it.galaxytrucker.model.adventurecards.cards.OpenSpace;
import it.polimi.it.galaxytrucker.model.design.observerPattern.Subject;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.model.managers.Player;

public class CalculateEnginePowerState extends State {
    private int numberOfPlayers;
    private int playerDecisions = 0;

    @Override
    public void enter(StateMachine fsm) {
        OpenSpace card = (OpenSpace) fsm;
        Subject subject = (Subject) fsm;
        numberOfPlayers = card.getNumberOfPlayer();
        subject.notifyObservers(new InputNeeded(card, getPlayerWhoChooses(card)));
    }

    @Override
    public void update(StateMachine fsm) {
        OpenSpace card = (OpenSpace) fsm;
        Subject subject = (Subject) fsm;
        
        playerDecisions++;
        if (allPlayersHaveResponded()) {
            card.changeState(new TravelState());
            return;
        }
        if (playerDecisions < numberOfPlayers) {
            subject.notifyObservers(new InputNeeded(card, getPlayerWhoChooses(card)));
        }
    }
    private boolean allPlayersHaveResponded() {
        return playerDecisions >= numberOfPlayers;
    }

    @Override
    public void exit(StateMachine fsm) {

    }

    private Player getPlayerWhoChooses(OpenSpace card) {
        List<Player> playersInFlightOrder = card.getPlayerOrder();

        return playersInFlightOrder.get(playerDecisions);
    }
}
