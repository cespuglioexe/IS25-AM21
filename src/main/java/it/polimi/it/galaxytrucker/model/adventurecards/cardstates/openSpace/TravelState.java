package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.openSpace;

import it.polimi.it.galaxytrucker.model.adventurecards.refactored.OpenSpace;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;

public class TravelState extends State {
    @Override
    public void enter(StateMachine fsm) {
        OpenSpace card = (OpenSpace) fsm;

        card.travel();
        changeState(fsm, new EndState());
    }

    @Override
    public void update(StateMachine fsm) {

    }

    @Override
    public void exit(StateMachine fsm) {

    }
}
