package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.smugglers;

import it.polimi.it.galaxytrucker.model.adventurecards.cardevents.UpdateStatus;
import it.polimi.it.galaxytrucker.model.adventurecards.cards.Smugglers;
import it.polimi.it.galaxytrucker.model.design.observerPattern.Subject;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.EndState;

public class StartState extends State {

    @Override
    public void enter(StateMachine fsm) {
        Smugglers card = (Smugglers) fsm;
        card.setPlayer();
        if(card.getCurrentPlayer() == null)
            fsm.changeState(new EndState());
        else fsm.changeState(new CalculateFirePowerState());
    }

    @Override
    public void update(StateMachine fsm) {

    }

    @Override
    public void exit(StateMachine fsm) {
        Smugglers card = (Smugglers) fsm;
        Subject subject = (Subject) fsm;

        subject.notifyObservers(new UpdateStatus(card));
    }
}
