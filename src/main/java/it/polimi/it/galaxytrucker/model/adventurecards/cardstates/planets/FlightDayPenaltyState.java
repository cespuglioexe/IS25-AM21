package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.planets;

import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.model.adventurecards.cards.Planets;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.EndState;

public class FlightDayPenaltyState extends State {

    @Override
    public void enter(StateMachine fsm) {
        Planets card = (Planets) fsm;

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
