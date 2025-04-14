package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.abandonedShip;

import it.polimi.it.galaxytrucker.model.adventurecards.refactored.AbandonedShip;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;

public class FlightDayPenaltyState extends State {

    @Override
    public void enter(StateMachine fsm) {
        AbandonedShip card = (AbandonedShip) fsm;

        card.applyFlightDayPenalty();
        changeState(fsm, new EndState());
    }

    @Override
    public void update(StateMachine fsm) {
        
    }

    @Override
    public void exit(StateMachine fsm) {
        
    }
    
}
