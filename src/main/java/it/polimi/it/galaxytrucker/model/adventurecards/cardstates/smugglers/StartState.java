package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.smugglers;

import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.smugglers.CalculateFirePowerState;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.smugglers.EndState;
import it.polimi.it.galaxytrucker.model.adventurecards.refactored.Smugglers;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;

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

    }
}
