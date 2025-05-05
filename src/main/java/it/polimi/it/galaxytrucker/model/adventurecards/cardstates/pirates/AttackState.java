package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.pirates;

import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.pirates.*;

import it.polimi.it.galaxytrucker.model.adventurecards.refactored.Pirates;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;

public class AttackState extends State {

    @Override
    public void enter(StateMachine fsm) {
        Pirates card = (Pirates) fsm;
        card.attack();
        changeState(fsm, new EvaluatePlayerState());
    }

    @Override
    public void update(StateMachine fsm) {

    }

    @Override
    public void exit(StateMachine fsm) {

    }
}
