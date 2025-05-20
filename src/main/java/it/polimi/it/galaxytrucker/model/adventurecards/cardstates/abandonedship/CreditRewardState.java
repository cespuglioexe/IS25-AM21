package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.abandonedship;

import it.polimi.it.galaxytrucker.model.adventurecards.cards.AbandonedShip;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;

public class CreditRewardState extends State {

    @Override
    public void enter(StateMachine fsm) {
        AbandonedShip card = (AbandonedShip) fsm;

        card.applyCreditReward();
        changeState(fsm, new CrewmatePenaltyState());
    }

    @Override
    public void update(StateMachine fsm) {
        
    }

    @Override
    public void exit(StateMachine fsm) {
        
    }
    
}
