package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.smugglers;

import it.polimi.it.galaxytrucker.model.adventurecards.cards.Smugglers;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;

public class CargoPenaltyState extends State {

    @Override
    public void enter(StateMachine fsm) {
        Smugglers card = (Smugglers) fsm;
        card.applyCargoPenalty();
        fsm.changeState(new StartState());
    }

    @Override
    public void update(StateMachine fsm) {

    }

    @Override
    public void exit(StateMachine fsm) {

    }
}

