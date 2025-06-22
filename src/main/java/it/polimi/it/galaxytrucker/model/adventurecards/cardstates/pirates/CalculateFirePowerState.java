package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.pirates;

import it.polimi.it.galaxytrucker.model.adventurecards.cardevents.InputNeeded;
import it.polimi.it.galaxytrucker.model.adventurecards.cards.Pirates;
import it.polimi.it.galaxytrucker.model.design.observerPattern.Subject;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;

public class CalculateFirePowerState extends State {

    @Override
    public void enter(StateMachine fsm) {
        Pirates card = (Pirates) fsm;
        Subject subject = (Subject) fsm;
        subject.notifyObservers(new InputNeeded(card, card.getPlayer()));
    }

    @Override
    public void update(StateMachine fsm) {
        fsm.changeState(new StartState());
    }

    @Override
    public void exit(StateMachine fsm) {

    }
}