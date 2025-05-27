package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.abandonedship;

import it.polimi.it.galaxytrucker.model.adventurecards.cardevents.InputNeeded;
import it.polimi.it.galaxytrucker.model.adventurecards.cards.AbandonedShip;
import it.polimi.it.galaxytrucker.model.design.observerPattern.Subject;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;

public class CrewmatePenaltyState extends State {
    private int penalty;
    private int appliedPenalty = 0;

    @Override
    public void enter(StateMachine fsm) {
        AbandonedShip card = (AbandonedShip) fsm;
        Subject subject = (Subject) fsm;

        penalty = card.getCrewmatePenalty();
        subject.notifyObservers(new InputNeeded(card));
    }

    @Override
    public void update(StateMachine fsm) {
        AbandonedShip card = (AbandonedShip) fsm;
        Subject subject = (Subject) fsm;
        if (++appliedPenalty == penalty) {
            changeState(fsm, new FlightDayPenaltyState());
        }
        subject.notifyObservers(new InputNeeded(card));
    }

    @Override
    public void exit(StateMachine fsm) {
        
    }
    
}
