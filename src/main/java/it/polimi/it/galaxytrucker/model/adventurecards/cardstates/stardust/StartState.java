package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.stardust;

import it.polimi.it.galaxytrucker.model.adventurecards.cardevents.UpdateStatus;
import it.polimi.it.galaxytrucker.model.adventurecards.cards.StarDust;
import it.polimi.it.galaxytrucker.model.design.observerPattern.Subject;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.EndState;

public class StartState extends State {
    @Override
    public void enter(StateMachine fsm) {
        StarDust card = (StarDust) fsm;
        card.applyEffect();
        fsm.changeState(new EndState());
    }

    @Override
    public void update(StateMachine fsm) {

    }

    @Override
    public void exit(StateMachine fsm) {
        StarDust card = (StarDust) fsm;
        Subject subject = (Subject) fsm;

        subject.notifyObservers(new UpdateStatus(card));
    }
}
