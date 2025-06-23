package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.combatzone;

import it.polimi.it.galaxytrucker.model.adventurecards.cardevents.UpdateStatus;
import it.polimi.it.galaxytrucker.model.adventurecards.cards.CombatZone;
import it.polimi.it.galaxytrucker.model.design.observerPattern.Subject;
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
        CombatZone card = (CombatZone) fsm;
        Subject subject = (Subject) fsm;

        subject.notifyObservers(new UpdateStatus(card));
    }
    
}
