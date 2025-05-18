package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.pirates;


import it.polimi.it.galaxytrucker.model.adventurecards.cards.Pirates;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;

public class CreditRewardState extends State {

    @Override
    public void enter(StateMachine fsm) {

    }

    @Override
    public void update(StateMachine fsm) {
        Pirates card = (Pirates) fsm;
        card.applyFlightDayPenalty();
        fsm.changeState(new EvaluatePlayerState());
    }

    @Override
    public void exit(StateMachine fsm) {

    }
}
