package it.polimi.it.galaxytrucker.model.adventurecards.cardstates;

import it.polimi.it.galaxytrucker.model.adventurecards.cardevents.CardResolved;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCard;
import it.polimi.it.galaxytrucker.model.design.observerPattern.Subject;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;

public class EndState extends State {

    @Override
    public void enter(StateMachine fsm) {
        AdventureCard card = (AdventureCard) fsm;
        Subject subject = (Subject) fsm;
        
        subject.notifyObservers(new CardResolved(card));
    }

    @Override
    public void update(StateMachine fsm) {
        
    }

    @Override
    public void exit(StateMachine fsm) {
        
    }
    
}
