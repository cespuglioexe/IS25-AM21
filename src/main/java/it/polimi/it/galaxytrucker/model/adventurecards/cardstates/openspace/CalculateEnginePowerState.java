package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.openspace;

import it.polimi.it.galaxytrucker.model.adventurecards.cardevents.InputNeeded;
import it.polimi.it.galaxytrucker.model.adventurecards.cards.OpenSpace;
import it.polimi.it.galaxytrucker.model.design.observerPattern.Subject;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;

public class CalculateEnginePowerState extends State {
    private int numberOfPlayers;
    private int playerDecisions = 0;

    @Override
    public void enter(StateMachine fsm) {
        OpenSpace card = (OpenSpace) fsm;
        Subject subject = (Subject) fsm;
        numberOfPlayers = card.getNumberOfPlayer();
        subject.notifyObservers(new InputNeeded(card));
    }

    @Override
    public void update(StateMachine fsm) {
        OpenSpace card = (OpenSpace) fsm;
        Subject subject = (Subject) fsm;
        if (allPlayersHaveResponded()) {
            card.changeState(new TravelState());
        }
        subject.notifyObservers(new InputNeeded(card));
    }
    private boolean allPlayersHaveResponded() {
        return ++playerDecisions == numberOfPlayers;
    }

    @Override
    public void exit(StateMachine fsm) {

    }
}
