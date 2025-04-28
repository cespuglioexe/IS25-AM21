package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.combatZone;

import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;

public class StartState extends State {

    @Override
    public void enter(StateMachine fsm) {
        changeState(fsm, new FlightDayPenaltyState());
    }

    @Override
    public void update(StateMachine fsm) {
        
    }

    @Override
    public void exit(StateMachine fsm) {
        
    }
    
}
