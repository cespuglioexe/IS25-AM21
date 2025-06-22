package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.slavers;

import it.polimi.it.galaxytrucker.model.adventurecards.cardevents.InputNeeded;
import it.polimi.it.galaxytrucker.model.adventurecards.cards.Slavers;
import it.polimi.it.galaxytrucker.model.design.observerPattern.Subject;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;

public class CrewmatePenaltyState extends State {

    @Override
    public void enter(StateMachine fsm) {
        Slavers card = (Slavers) fsm;
        Subject subject = (Subject) fsm;
        subject.notifyObservers(new InputNeeded(card, card.getCurrentPlayer()));
    }

    @Override
    public void update(StateMachine fsm) {
        fsm.changeState(new StartState());
    }

    @Override
    public void exit(StateMachine fsm) {

    }
}
