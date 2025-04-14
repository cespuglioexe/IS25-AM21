package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.abandonedShip;

import it.polimi.it.galaxytrucker.model.adventurecards.refactored.AbandonedShip;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;

public class CrewmatePenaltyState extends State {
    private int penalty;
    private int appliedPenalty = 0;

    @Override
    public void enter(StateMachine fsm) {
        AbandonedShip card = (AbandonedShip) fsm;

        penalty = card.getCrewmatePenalty();
    }

    @Override
    public void update(StateMachine fsm) {
        if (++appliedPenalty == penalty) {
            changeState(fsm, new FlightDayPenaltyState());
        }
    }

    @Override
    public void exit(StateMachine fsm) {
        
    }
    
}
