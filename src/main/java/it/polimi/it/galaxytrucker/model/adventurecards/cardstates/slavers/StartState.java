package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.slavers;

import it.polimi.it.galaxytrucker.model.adventurecards.cards.Slavers;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.EndState;

public class StartState extends State {

    @Override
    public void enter(StateMachine fsm) {
        Slavers card = (Slavers) fsm;
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

    }
}
