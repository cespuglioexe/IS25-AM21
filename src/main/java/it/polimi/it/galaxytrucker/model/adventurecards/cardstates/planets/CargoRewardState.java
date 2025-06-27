package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.planets;

import it.polimi.it.galaxytrucker.model.adventurecards.cardevents.InputNeeded;
import it.polimi.it.galaxytrucker.model.adventurecards.cards.Planets;
import it.polimi.it.galaxytrucker.model.design.observerPattern.Subject;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;

public class CargoRewardState extends State {

    @Override
    public void enter(StateMachine fsm) {
        Planets card = (Planets) fsm;
        Subject subject = (Subject) fsm;

        card.initializeFirstPlayer();
        subject.notifyObservers(new InputNeeded(card, card.getCurrentPlayer()));
    }

    @Override
    public void update(StateMachine fsm) {

    }

    @Override
    public void exit(StateMachine fsm) {
        
    }
    
}
