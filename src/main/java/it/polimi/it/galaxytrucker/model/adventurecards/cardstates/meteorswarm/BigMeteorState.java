package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.meteorswarm;

import it.polimi.it.galaxytrucker.model.adventurecards.cardevents.InputNeeded;
import it.polimi.it.galaxytrucker.model.adventurecards.cards.MeteorSwarm;
import it.polimi.it.galaxytrucker.model.design.observerPattern.Subject;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;

public class BigMeteorState extends State {

    @Override
    public void enter(StateMachine fsm) {
        MeteorSwarm card = (MeteorSwarm) fsm;
        Subject subject = (Subject) fsm;
        card.aimAtCoordsWith(card.getCurrentMeteor());
        subject.notifyObservers(new InputNeeded(card));
    }

    @Override
    public void update(StateMachine fsm) {
        fsm.changeState(new AttackState());
    }

    @Override
    public void exit(StateMachine fsm) {

    }
}
