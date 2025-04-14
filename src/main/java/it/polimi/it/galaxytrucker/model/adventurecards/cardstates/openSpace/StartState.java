package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.openSpace;

import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.planets.ParticipationState;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;

public class StartState extends State {
    @Override
    public void enter(StateMachine fsm) {
        fsm.changeState(new CalculateEnginePowerState());
    }

    @Override
    public void update(StateMachine fsm) {

    }

    @Override
    public void exit(StateMachine fsm) {

    }
}

