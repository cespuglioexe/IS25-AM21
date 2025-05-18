package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.openspace;

import it.polimi.it.galaxytrucker.model.adventurecards.cards.OpenSpace;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.EndState;

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
