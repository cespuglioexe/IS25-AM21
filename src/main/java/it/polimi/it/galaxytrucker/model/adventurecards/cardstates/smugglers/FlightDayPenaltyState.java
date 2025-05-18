package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.smugglers;

import it.polimi.it.galaxytrucker.model.adventurecards.cards.Smugglers;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.EndState;

public class FlightDayPenaltyState extends State {

    @Override
    public void enter(StateMachine fsm) {
        Smugglers card = (Smugglers) fsm;

        card.applyFlightDayPenalty();
        changeState(fsm, new EndState());
    }

    @Override
    public void update(StateMachine fsm) {

    }

    @Override
    public void exit(StateMachine fsm) {

    }

}
