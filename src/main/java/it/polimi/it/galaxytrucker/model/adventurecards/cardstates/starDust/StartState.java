package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.starDust;

import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.starDust.EndState;
import it.polimi.it.galaxytrucker.model.adventurecards.refactored.StarDust;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;

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
    }
}
