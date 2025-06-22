package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.combatzone;

import it.polimi.it.galaxytrucker.model.adventurecards.cardevents.InputNeeded;
import it.polimi.it.galaxytrucker.model.adventurecards.cards.CombatZone;
import it.polimi.it.galaxytrucker.model.design.observerPattern.Subject;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;

public class CrewmatePenaltyState extends State {
    private int penalty;
    private int appliedPenalty = 0;

    @Override
    public void enter(StateMachine fsm) {
        CombatZone card = (CombatZone) fsm;
        Subject subject = (Subject) fsm;
        penalty = card.getCrewmatePenalty();
        subject.notifyObservers(new InputNeeded(card, card.getPlayer()));
    }

    @Override
    public void update(StateMachine fsm) {
        CombatZone card = (CombatZone) fsm;
        Subject subject = (Subject) fsm;
        if (++appliedPenalty == penalty) {
            changeState(fsm, new CannonSelectionState());
        }
        subject.notifyObservers(new InputNeeded(card, card.getPlayer()));
    }

    @Override
    public void exit(StateMachine fsm) {
        
    }
    
}
