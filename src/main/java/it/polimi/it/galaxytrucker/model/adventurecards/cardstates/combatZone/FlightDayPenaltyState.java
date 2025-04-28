package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.combatZone;

import it.polimi.it.galaxytrucker.model.adventurecards.refactored.CombatZone;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;

public class FlightDayPenaltyState extends State {

    @Override
    public void enter(StateMachine fsm) {
        CombatZone card = (CombatZone) fsm;

        card.applyFlightDayPenalty();

        changeState(fsm, new EngineSelectionState());
    }

    @Override
    public void update(StateMachine fsm) {
        
    }

    @Override
    public void exit(StateMachine fsm) {
        
    }
    
}
