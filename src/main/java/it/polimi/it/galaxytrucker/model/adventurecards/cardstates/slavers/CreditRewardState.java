package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.slavers;

import it.polimi.it.galaxytrucker.model.adventurecards.refactored.Slavers;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;

public class CreditRewardState extends State {
    @Override
    public void enter(StateMachine fsm) {

    }

    @Override
    public void update(StateMachine fsm) {
        Slavers card = (Slavers) fsm;
        card.applyFlightDayPenalty();
        fsm.changeState(new EndState());
    }

    @Override
    public void exit(StateMachine fsm) {

    }
}
