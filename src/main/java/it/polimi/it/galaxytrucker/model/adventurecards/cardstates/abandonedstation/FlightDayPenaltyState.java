package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.abandonedstation;

import it.polimi.it.galaxytrucker.model.adventurecards.cards.AbandonedStation;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.EndState;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;

public class FlightDayPenaltyState extends State {

    @Override
    public void enter(StateMachine fsm) {
        AbandonedStation card = (AbandonedStation) fsm;

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