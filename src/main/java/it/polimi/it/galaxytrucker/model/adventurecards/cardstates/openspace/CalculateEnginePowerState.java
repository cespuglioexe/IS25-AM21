package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.openspace;

import it.polimi.it.galaxytrucker.model.adventurecards.cards.OpenSpace;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;

public class CalculateEnginePowerState extends State {
    private int numberOfPlayers;
    private int playerDecisions = 0;

    @Override
    public void enter(StateMachine fsm) {
        OpenSpace card = (OpenSpace) fsm;
        numberOfPlayers = card.getNumberOfPlayer();
    }

    @Override
    public void update(StateMachine fsm) {
        if (allPlayersHaveResponded()) {
            OpenSpace card = (OpenSpace) fsm;
            card.changeState(new TravelState());
        }
    }
    private boolean allPlayersHaveResponded() {
        return ++playerDecisions == numberOfPlayers;
    }

    @Override
    public void exit(StateMachine fsm) {

    }
}
