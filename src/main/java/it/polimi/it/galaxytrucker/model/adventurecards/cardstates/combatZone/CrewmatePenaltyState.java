package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.combatZone;

import it.polimi.it.galaxytrucker.model.adventurecards.refactored.CombatZone;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;

public class CrewmatePenaltyState extends State {
    private int penalty;
    private int appliedPenalty = 0;

    @Override
    public void enter(StateMachine fsm) {
        CombatZone card = (CombatZone) fsm;

        penalty = card.getCrewmatePenalty();
    }

    @Override
    public void update(StateMachine fsm) {
        if (++appliedPenalty == penalty) {
            changeState(fsm, new CannonSelectionState());
        }
    }

    @Override
    public void exit(StateMachine fsm) {
        
    }
    
}
