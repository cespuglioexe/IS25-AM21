package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.pirates;

import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;

public class CalculateFirePowerState extends State {

    @Override
    public void enter(StateMachine fsm) {
    }

    @Override
    public void update(StateMachine fsm) {
        fsm.changeState(new StartState());
    }

    @Override
    public void exit(StateMachine fsm) {

    }
}